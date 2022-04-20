package com.dailycodechallenge.jsontocsv.jsontocsv.controller;


import com.dailycodechallenge.jsontocsv.jsontocsv.model.Response;
import com.dailycodechallenge.jsontocsv.jsontocsv.util.JFlat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
public class JsonToCsvController {

   /* @GetMapping("/generatecsv/{delimiter}")
    public  void generateCSVFile(@PathVariable("delimiter") String delimiter)
    {
        System.out.println("Generated" );
    }*/

   @Autowired
   RestTemplate restTemplate;

    @GetMapping("/generatecsv/{name}")
    public ResponseEntity<?> generateCSVFile(@PathVariable("name") String name)
    {
        System.out.println(name );
        try {
            File   resource=new ClassPathResource("/json/users1.json").getFile( );
            String str    =new String(Files.readAllBytes(resource.toPath( )));
            //String url = "https://jsonplaceholder.typicode.com/todos";
           // String str = restTemplate.getForObject(url,String.class);
            System.out.println(str );

            Path pathToFile= Paths.get("C:/Users/Tek-Soft-Lead/Desktop/usbank/transaction_data.csv");
            BufferedReader br = Files.newBufferedReader(pathToFile,
                    StandardCharsets.US_ASCII);
            String line = br.readLine();
            String[] attributes = null;
            if (line != null) {
                attributes = line.split(",");
            }


            File file = new File("C:/Users/Tek-Soft-Lead/Desktop/usbank/Transaction API DB.xlsx");
            MultipartFile excel = new MockMultipartFile("test.xlsx", new FileInputStream(file));

            XSSFWorkbook workbook= new XSSFWorkbook(excel.getInputStream());
            XSSFSheet    sheet   = workbook.getSheetAt(0);
            Map<String,String> databaseMapper = new HashMap<>();

            for(int i=0; i<sheet.getPhysicalNumberOfRows();i++) {
                XSSFRow row= sheet.getRow(i);
                  databaseMapper.put("/"+row.getCell(1).toString().replace(".","/")
                          ,row.getCell(0).toString());
            }



            JFlat flatMe=new JFlat(str,databaseMapper,attributes);
           // List<Object[]> json2csv=flatMe.json2Sheet( ).getJsonAsSheet( );
            String outputFileName = new SimpleDateFormat("ddMMyyyyHHmmss'.csv'", Locale.getDefault()).format(new Date());
            //flatMe.json2Sheet().headerSeparator("/").write2csv(outputFileName);
            flatMe.json2Sheet().write2csv(outputFileName);
           // System.out.println(outputFileName );
//directly write the JSON document to CSV but with delimiter
            flatMe.json2Sheet().write2csv(outputFileName, ',');



        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new Response("Conversion Failed !!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response("Sucessfully Converted !!"), HttpStatus.OK);
    }


    File [] files = new File("path/to/folder/").listFiles(obj -> obj.isFile() && obj.getName().endsWith(".csv"));

    @GetMapping("/generatecsvs")
    public ResponseEntity<?> generateCSVFiles()
    {
        try {
            System.out.println("Entered" );
            File [] files = new File("C:/Users/Tek-Soft-Lead/Desktop/usbank/Json2Flat-master/Json_To_Csv/src/main/resources/json")
                    .listFiles(obj -> obj.isFile() && obj.getName().endsWith(".json"));
            System.out.println("Entered2"+files );
            System.out.println("Length"+files.length );
            for (File file : files) {
                 File   resource=new ClassPathResource("/json/users1.json").getFile( );
                 String str    =new String(Files.readAllBytes(resource.toPath( )));
                System.out.println(file.getName());
                JFlat flatMe=new JFlat(str,new HashMap<>(),null);
                // List<Object[]> json2csv=flatMe.json2Sheet( ).getJsonAsSheet( );
                String outputFileName=new SimpleDateFormat("ddMMyyyyHHmmss'.csv'", Locale.getDefault( )).format(new Date( ));
                //flatMe.json2Sheet().headerSeparator("/").write2csv(outputFileName);
               outputFileName = file.getName().concat(outputFileName);
                flatMe.json2Sheet( ).write2csv(outputFileName);
                System.out.println(outputFileName);
//directly write the JSON document to CSV but with delimiter
                flatMe.json2Sheet( ).write2csv(outputFileName, ',');

            }

        }
        catch (Exception e)
        {
            return new ResponseEntity<>(new Response("Conversion Failed !!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Response("Sucessfully Converted !!"), HttpStatus.OK);
    }
}
