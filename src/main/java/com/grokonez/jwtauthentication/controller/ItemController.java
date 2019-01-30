package com.grokonez.jwtauthentication.controller;




import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.grokonez.jwtauthentication.model.Item;
import com.grokonez.jwtauthentication.repository.ConsignmentRepository;
import com.grokonez.jwtauthentication.repository.ItemRepository;
import javassist.NotFoundException;


import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.RED;
import org.apache.poi.ss.usermodel.*;


import org.hibernate.service.spi.ServiceException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Timed;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path="/api")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ConsignmentRepository consignmentRepository;


    //method that return all items with ConsignmentId
    @GetMapping("/consignment/{consignmentId}/items")
    public List<Item> getItemByConsignmentId(@PathVariable (value = "consignmentId") Long consignmentId) {

        return itemRepository.findByConsignmentId(consignmentId);
    }


    //metodo para hacer el json personalizado para el archivo excel
    @ResponseBody
    @GetMapping(path="consignment/{consignmentId}/items/excel",produces="application/json")
    public JSONArray getJsonCustom(@PathVariable (value = "consignmentId") Long consignmentId) {

        JSONObject jsonObject= new JSONObject();
        JSONArray array = new JSONArray();

        String posito = null;
        Integer resolucion = null;
        String orden_compra = null;
        Integer numero_fact = null;
        String Rut = null;
        String proveedor = null;
        String detalle = null;
        BigInteger monto = null;
        String item_presupuestario = null;
        String nombre_item = null;

        List<Item> items = itemRepository.findByConsignmentId(consignmentId);

        for(Item item : items){
            jsonObject = new JSONObject();

            posito= item.getType_gast();
            resolucion = item.getResolution();
            orden_compra= item.getPurchase_order();
            numero_fact = item.getBill_number();
            Rut = item.getRut();
            proveedor = item.getProvider();
            detalle= item.getDetail();
            monto = item.getMoney();
            item_presupuestario = item.getBudget_item();
            nombre_item = item.getName_item();


            jsonObject.put("Tipo de Gasto",posito);

            jsonObject.put("Resolucion",resolucion);
            jsonObject.put("Orden de Compra",orden_compra);
            jsonObject.put("NºFactura o boleta",numero_fact);
            jsonObject.put("Rut",Rut);
            jsonObject.put("Detalle",detalle);
            jsonObject.put("Monto", monto);
            jsonObject.put("Item Presupuestario", item_presupuestario);
            jsonObject.put("Nombre Item", nombre_item);


            //jsonObject.put();
            array.add(jsonObject);


        }

        return array;

    }
    //method create a json with library gson

    @ResponseBody
    @GetMapping(path="consignment/{consignmentId}/exportable",produces={"text/plain","application/json"})
    public JsonObject getGson(@PathVariable (value = "consignmentId") Long consignmentId) {
        JsonObject person = new JsonObject();
        //JsonObject item_personalizado = new JsonObject();

        person.addProperty("firstName", "Sergey");
        person.addProperty("lastName", "Kargopolov");
        person.addProperty("id", "1");
        person.addProperty("age", "20");
        String algo = person.toString();
        String nuevo = new String();
        List <ArrayList> lista = new ArrayList();
        List<Item> items = itemRepository.findByConsignmentId(consignmentId);
        Gson gsonBuilder = new GsonBuilder().create();
        // Convert Java Map into JSON
        //Map personMap = new HashMap();
        //personMap.put("firstName", "Sergey");
        //personMap.put("lastName", "Kargopolov");
        String jsonFromJavaMap = new String();
        Map personMap = new HashMap();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("product-id", "1");
        jsonObject.addProperty("description", "2");
        jsonObject.addProperty("image-url","3");

        //return jsonObject;

        for(Item item : items){

            JsonObject item_personalizado = new JsonObject();
            personMap.put("tipo de gasto", item.getType_gast());
            //lista.add(new ArrayList());
            item_personalizado.addProperty("tipo de gasto",item.getType_gast());
            nuevo = item_personalizado.toString();
            jsonFromJavaMap = gsonBuilder.toJson(personMap);

        }

        return jsonObject;


    }
    //method return excel

    @ResponseBody
    @GetMapping(path="consignment/{consignmentId}/export")
    public ResponseEntity<Object> getExport(@PathVariable (value = "consignmentId") Long consignmentId) throws IOException {
        String lid = consignmentId.toString();
        String name_file= consignmentRepository.findNameConsignment(consignmentId) + "NºRemesa" + lid + ".xls";

        List<Item> items = itemRepository.findByConsignmentId(consignmentId);
        BigInteger Total = itemRepository.findTotalConsignmentById(consignmentId);
        double totalItems = Total.doubleValue();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("lista de items");

        //creando filas
        Row rowHeading = sheet.createRow(0);
        rowHeading.createCell(0).setCellValue("tipo de gasto");
        rowHeading.createCell(1).setCellValue("resolucion");
        rowHeading.createCell(2).setCellValue("orden de compra");
        rowHeading.createCell(3).setCellValue("NºFactura o boleta");
        rowHeading.createCell(4).setCellValue("Rut");
        rowHeading.createCell(5).setCellValue("Proveedor");
        rowHeading.createCell(6).setCellValue("Detalle");
        rowHeading.createCell(7).setCellValue("Monto");
        rowHeading.createCell(8).setCellValue("Item Presupuestario");
        rowHeading.createCell(9).setCellValue("Nombre Item");
        for(int i=0; i<10;i++){
            CellStyle styleRowHeading = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short)11);
            styleRowHeading.setFont(font);
            //styleRowHeading.setVerticalAlignment(CellStyle.ALIGN_CENTER);
            rowHeading.getCell(i).setCellStyle(styleRowHeading);
        }


        int r=1;
        for(Item item : items){
            Row row = sheet.createRow(r);
            //tipo de gasto
            Cell cellId = row.createCell(0);
            cellId.setCellValue(item.getType_gast());
            //resolucion
            Cell cellResolution = row.createCell(1);
            cellResolution.setCellValue(item.getResolution());
            //orden de compra
            Cell cellPurchase = row.createCell(2);
            cellPurchase.setCellValue(item.getPurchase_order());
            //numero factyra
            Cell cellfactura = row.createCell(3);
            if(item.getBill_number()== null ){
                cellfactura.setCellValue(0);
            }
            else{
                cellfactura.setCellValue(item.getBill_number());
            }

            //rut
            Cell cellrut = row.createCell(4);
            cellrut.setCellValue(item.getRut());
            //proveedor
            Cell cellproveedor = row.createCell(5);
            cellproveedor.setCellValue(item.getProvider());
            //detalle
            Cell celldetalle = row.createCell(6);
            celldetalle.setCellValue(item.getDetail());
            //dinero
            Cell cellmonto = row.createCell(7);

            double dinero = (item.getMoney()).doubleValue();
            cellmonto.setCellValue(dinero);
            //item presupuestario
            Cell cellitem= row.createCell(8);
            cellitem.setCellValue(item.getBudget_item());
            //nombre item
            Cell cellnameitem= row.createCell(9);
            cellnameitem.setCellValue(item.getName_item());
            r++;

        }
        //total columna
        Row rowTotal = sheet.createRow(items.size()+5);
        Cell cellTotal = rowTotal.createCell(6);
        cellTotal.setCellValue("Total");
        CellStyle styleTotal = workbook.createCellStyle();
        Font FontTextTotal = workbook.createFont();
        FontTextTotal.setBold(true);
        FontTextTotal.setFontHeightInPoints((short)10);
        FontTextTotal.setColor(HSSFColor.RED.index);
        styleTotal.setFont(FontTextTotal);
        cellTotal.setCellStyle(styleTotal);
        //escribir el numero totañ
        Cell numeroTotal = rowTotal.createCell(7);
        numeroTotal.setCellValue(totalItems);

        //formato de las filas de acuerdo al nombre
        for (int i=0; i<10; i++){
            sheet.autoSizeColumn(i);
        }
        String filename = "listItems.xls";

        FileOutputStream out = new FileOutputStream(new File(name_file));
        workbook.write(out);
        out.close();

        File file = new File(name_file);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename= \"%s\"",file.getName()));
        headers.add("Cache-Control", "no-cache , no-store , must-revalidate");
        headers.add("Pragma","no-cache");
        headers.add("Expires","0");
        ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);

        return responseEntity;

    }

    @GetMapping(path = "consignment/{consignmentId}/item/{itemId}")
    public @ResponseBody
    Item getItemByConsignmentIdByItemId(@PathVariable Long itemId,
                                        @PathVariable Long consignmentId){
        //long lid = id.longValue();
        return itemRepository.findByItemId(consignmentId,itemId);

    }

    //method that return sum items of a consignment

    @GetMapping("/consignment/{consignmentId}/total")
    public BigInteger getSumTotalOfItems(@PathVariable (value = "consignmentId") Long consignmentId) {
        if(itemRepository.findTotalConsignmentById(consignmentId) != null ){

            return itemRepository.findTotalConsignmentById(consignmentId);
        }
        else{
            return null;

        }


    }


    //method post that return new item
    //consumes = "multipart/form-data"
    //@RequestMapping(consumes = "multipart/form-data")
    //@ExceptionHandler(MultipartException.class)
    @PostMapping(value = "/consignment/{consignmentId}/item/new")

    public Item addItem(@PathVariable Long consignmentId,

                        @Valid @RequestBody Item item) throws NotFoundException {
        return consignmentRepository.findById(consignmentId)
                .map(consignment -> {
                    item.setConsignment(consignment);

                    return itemRepository.save(item);
                }).orElseThrow(()-> new NotFoundException("Remesa no existe!"));

    }


    //method put that update a item
    @PutMapping("/consignment/{consignmentId}/item/{itemId}")
    public Item updateItem(@PathVariable Long consignmentId,
                           @PathVariable Long itemId,
                           @Valid @RequestBody Item itemUpdated) throws NotFoundException {

        if(!consignmentRepository.existsById(consignmentId)) {
            throw new NotFoundException("Remesa not found!");
        }

        return itemRepository.findById(itemId)
                .map(item -> {
                    item.setBill_number(itemUpdated.getBill_number());
                    item.setBudget_item(itemUpdated.getBudget_item());
                    item.setName_item(itemUpdated.getName_item());
                    item.setProvider(itemUpdated.getProvider());
                    item.setPurchase_order(itemUpdated.getPurchase_order());
                    item.setResolution(itemUpdated.getResolution());
                    item.setRut(itemUpdated.getRut());
                    item.setMoney(itemUpdated.getMoney());
                    item.setDetail(itemUpdated.getDetail());
                    item.setStatus(itemUpdated.getStatus());
                    item.setType_gast(itemUpdated.getType_gast());
                    return itemRepository.save(item);
                }).orElseThrow(() -> new NotFoundException("Item not found!"));
    }


    //method for change the status of item
    @PutMapping("/consignment/{consignmentId}/item/{itemId}/{status}")
    public Item updateStatusItem(@PathVariable Long consignmentId,
                                 @PathVariable Long itemId,
                                 @PathVariable String status,
                                 @Valid @RequestBody Item itemUpdated) throws NotFoundException {

        if(!consignmentRepository.existsById(consignmentId)) {
            throw new NotFoundException("Remesa not found!");
        }

        return itemRepository.findById(itemId)
                .map(item -> {

                    if(item.getStatus() == "rechazado" ){
                        item.setStatus(status);

                    }
                    else if(item.getStatus()=="aprobado"){
                        item.setStatus(status);
                    }
                    else{
                        item.setStatus(status);
                    }
                    //item.setStatus(itemUpdated.getStatus());

                    return itemRepository.save(item);
                }).orElseThrow(() -> new NotFoundException("Item not found!"));
    }

    //method para update un item, status: aceptado o rechazado

    //method that delete a item
    @DeleteMapping("/consignment/{consignmentId}/item/{itemId}")
    public String deleteAssignment(@PathVariable Long consignmentId,
                                   @PathVariable Long itemId) throws NotFoundException {

        if(!consignmentRepository.existsById(consignmentId)) {
            throw new NotFoundException("Remesa not found!");
        }

        return itemRepository.findById(itemId)
                .map(item -> {
                    itemRepository.delete(item);
                    return "Deleted Successfully!";
                }).orElseThrow(() -> new NotFoundException("item not found!"));
    }

}
