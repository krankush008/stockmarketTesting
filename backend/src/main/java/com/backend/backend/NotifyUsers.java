package com.backend.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import org.springframework.scheduling.annotation.Scheduled;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.List;
import java.util.regex.Matcher;


@Component
public class NotifyUsers implements CommandLineRunner {
    /* 
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    scheduler.@scheduleAtFixedRate(() -> {
        // Call your function here
    }, 0, 30*1000);
    Timer timer = new Timer(true);
        
    // Schedule the task to run every 30 seconds
    */

    private final AlertRepository alertRepository;
    private final BondRepository bondRepository;
    private final Bonds1Repository bonds1Repository;
    private final AlertsRepository alertsRepository;

    public NotifyUsers(AlertRepository alertRepository, BondRepository bondRepository, Bonds1Repository bonds1Repository, AlertsRepository alertsRepository) {
        this.alertRepository = alertRepository;
        this.bondRepository = bondRepository;
        this.bonds1Repository = bonds1Repository;
        this.alertsRepository = alertsRepository;
    }

    public void storeBonds(List<Bonds> bonds) {
        try {
            bondRepository.saveAll(bonds);
            System.out.println("Bonds successfully stored in the database.");
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
    private void parseHtmlToBonds() {
        
        try {
           String url = "https://www.icicidirect.com/bonds/exchange-traded-bonds-ncds";
            // Create a URL object
            URL apiUrl = new URL(url);
            System.out.println("ankush");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();
            String maturityValue="";
            String ISIN_no="";
            String creditScore="";

            // Check if the request was successful (status code 200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the server
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // Print the response
                    //System.out.println("Response: " + response.toString());
                    String htmlContent = response.toString();

                    // Parse the HTML content with Jsoup
                    Document doc = Jsoup.parse(htmlContent);

                    // Get the table row by its ID (replace 'yourTableRowId' with the actual ID)
                    Element tableRow = doc.getElementById("NCDBondTbody");

                    if (tableRow != null) {
                        // Get all rows in the table
                        Elements rows = tableRow.select("tr");
            
                        // Iterate over each row
                        for (Element row : rows) {
                            
                            
                            Elements cells = row.select("td");
            
                            // Check if the row has at least 5 cells
                            if (cells.size() >= 1) {
                                // Get the 5th value (index 4, as indexing starts from 0)
                                String firstVal = cells.get(0).toString();
                                Document doc1 = Jsoup.parse(firstVal);
                                Element aTag = doc1.select("td.fund_name a").first();
                                Element tdElement = doc1.select("td.fund_name").first();
                                if (tdElement != null) {
                                    // Select the <p> element within the <td> element
                                    Element pElement = tdElement.select("p").first();
                        
                                    // Check if the <p> element exists
                                    
                                    if (pElement != null) {
                                        // Extract the text content of the <p> element
                                        String paragraphText = pElement.text();
                                        /* 
                                        String regex = "[A-Za-z]+[\\\\s]*[A-Za-z]*[\\\\+]*[\\\\-]*[\\\\(]*[A-Za-z]*[\\\\)]*";
                                        Pattern pattern = Pattern.compile(regex);

                                        // Create a Matcher object and apply the pattern to the input text
                                        Matcher matcher = pattern.matcher(paragraphText);

                                        // Check if a match is found
                                        if (matcher.find()) {
                                            // Extract the matched credit rating
                                            String creditRating = matcher.group(1);
                                            System.out.println("Credit Rating: " + creditRating);
                                        } else {
                                            System.out.println("No credit rating found in the text.");
                                        }
                                        */
                                        creditScore=paragraphText;
                                        System.out.println("Paragraph Text: " + paragraphText);
                                    } else {
                                        System.out.println("<p> element not found within <td>.");
                                    }
                                } else {
                                    System.out.println("<td> element with class 'fund_name' not found.");
                                }

                                // Check if the <a> tag exists
                                if (aTag != null) {
                                    // Get the value of the 'href' attribute
                                    String hrefValue = aTag.attr("href");
                                    System.out.println("Href Value: " + hrefValue);
                                    String bondUrl = hrefValue;
                                    Pattern pattern = Pattern.compile("/([^/]+)$");
                                    Matcher matcher = pattern.matcher(bondUrl);

                                    // Check if the pattern matches and extract the ISIN

        // Create a matcher with the link

                                    // Find the last occurrence of the pattern in the link
                                    if (matcher.find()) {
                                        // Extract the captured group (ine031a07840 in this case)
                                        String extractedCode = matcher.group(1);
                                        String ISIN=extractedCode;
                                        ISIN_no=ISIN;
                                        System.out.println("Extracted Code: " + extractedCode);
                                    } else {
                                        System.out.println("Code not found in the link.");
                                    }
                                    

                                    URL apiBondUrl = new URL(bondUrl);
                                    HttpURLConnection connection1 = (HttpURLConnection) apiBondUrl.openConnection();

                                    // Set the request method to GET
                                    connection1.setRequestMethod("GET");

                                    // Get the response code
                                    int responseCode1 = connection1.getResponseCode();


                                    if (responseCode1 == HttpURLConnection.HTTP_OK) {
                                        // Read the response from the server
                                        try (BufferedReader reader1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()))) {
                                            String line1;
                                            StringBuilder response1 = new StringBuilder();
                        
                                            while ((line1 = reader1.readLine()) != null) {
                                                response1.append(line1);
                                            }
                        
                                            // Print the response
                                            //System.out.println("Response: " + response.toString());
                                            String htmlContent1 = response1.toString();
                                            Document document = Jsoup.parse(htmlContent1);
                                            Element overviewDetailsDiv = document.selectFirst("div.overview_details");
                                           // System.out.println("myResponse "+overviewDetailsDiv.toString());
                                            Document document1 = Jsoup.parse(overviewDetailsDiv.toString());

        // Select the element with class "maturityDate"
                                            Element maturityDateElement = document1.select(".maturityDate").first();

                                            if (maturityDateElement != null) {
                                                // Extract the text content from the element
                                                String maturityDate = maturityDateElement.text();
                                                maturityValue=maturityDate;
                                                System.out.println("Maturity Date: " + maturityDate);
                                            } else {
                                                System.out.println("Maturity date not found in the HTML.");
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                } else {
                                    System.out.println("No <a> tag found.");
                                }
                            Bonds1 bonds1=new Bonds1(ISIN_no, maturityValue, creditScore);

                            bonds1Repository.save(bonds1);
                            } else {
                                System.out.println("Row doesn't have enough cells.");
                            }
                            
                        }
                    } else {
                        System.out.println("Table not found.");
                    }

                    // Check if the table row exists
                    if (tableRow != null) {
                        // Do something with the table row, e.g., print its HTML content
                       // System.out.println("Table Row HTML: " + tableRow.outerHtml());
                    } else {
                        System.out.println("Table Row not found.");
                    }
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //return bonds;
    }
/* 
    public ResponseEntity<String> storeBonds1() {
        // Assuming you have a method to fetch bonds (parseHtmlToBonds is just an example)
        List<Bonds> bonds = parseHtmlToBonds();

        if (bonds != null && !bonds.isEmpty()) {
            storeBonds(bonds);
            return ResponseEntity.ok("Bonds stored successfully.");
        } else {
            return ResponseEntity.badRequest().body("Unable to fetch bonds or bonds list is empty.");
        }
    }
*/
    public ResponseEntity<List<Alerts>> getUsersByXIRR(String bonds_id) {
        // Assuming you have a method to get Bonds.xirr based on userId
        BigDecimal xirr = getBondsXIRR(7);

        if (xirr != null) {
            // Query to get Alerts where XIRR < Bonds.xirr
            System.out.println("akjhdfgvbjh"+alertsRepository.findByBondsIdAndXirrLessThan(bonds_id, xirr));
            System.out.println("khan");
            List<Alerts> alerts = alertsRepository.findByBondsIdAndXirrLessThan(bonds_id, xirr);
            for(int i=0;i<alerts.size();i++){
                System.out.println("kumar ");
            }

            if (!alerts.isEmpty()) {
                return ResponseEntity.ok(alerts);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            // Handle the case when Bonds.xirr is not available
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public void getAllAlerts() {
        System.out.println("kkankushkumar");
        //List<Alert> ans=alertRepository.findAll();
        System.out.println(alertRepository.findAll());
        System.out.println("kkrAnkush");
    }
    // Method to simulate getting Bonds.xirr based on userId
    private BigDecimal getBondsXIRR(int i) {
        // Your logic to get Bonds.xirr based on userId
        // Replace this with your actual logic
        return BigDecimal.valueOf(i);
    }
    
    public void func(){
        try {
            // Specify the URL for the GET request
           // System.out.println("ankush");
            //String url = "https://www.thefixedincome.com/productlistajax?from_yield=&to_yield=&from_maturity=0&to_maturity=40&list_type=grad&product_type=1&page=2&search=&yieldSort=&url_filter=&maturityYearmax=40";
            String url = "https://www.icicidirect.com/bonds/exchange-traded-bonds-ncds";
            // Create a URL object
            URL apiUrl = new URL(url);
            System.out.println("ankush");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Check if the request was successful (status code 200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response from the server
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    // Print the response
                    //System.out.println("Response: " + response.toString());
                    String htmlContent = response.toString();

                    // Parse the HTML content with Jsoup
                    Document doc = Jsoup.parse(htmlContent);

                    // Get the table row by its ID (replace 'yourTableRowId' with the actual ID)
                    Element tableRow = doc.getElementById("NCDBondTbody");

                    if (tableRow != null) {
                        // Get all rows in the table
                        Elements rows = tableRow.select("tr");
            
                        // Iterate over each row
                        for (Element row : rows) {
                            // Get all cells in the row

                            /*
                            Element firstCell = row.selectFirst("td");

                            // Check if the row has at least 1 cell
                            if (firstCell != null) {
                                // Get the HTML content of the first cell
                                String firstCellValueHtml = firstCell.html();
                                Document doc1 = Jsoup.parse(htmlContent);

                                // Get the <a> tag inside the <td>
                                Element aTag = doc1.select("td.fund_name a").first();

                                // Check if the <a> tag exists
                                if (aTag != null) {
                                    // Get the value of the 'href' attribute
                                    String hrefValue = aTag.attr("href");
                                    System.out.println("Href Value: " + hrefValue);
                                } else {
                                    System.out.println("No <a> tag found.");
                                }
                              //  System.out.println("HTML Element for the First Value: " + firstCellValueHtml);
                            } else {
                                System.out.println("Row doesn't have enough cells.");
                            }
                            */
                            
                            Elements cells = row.select("td");
            
                            // Check if the row has at least 5 cells
                            if (cells.size() >= 1) {
                                // Get the 5th value (index 4, as indexing starts from 0)
                                String firstVal = cells.get(0).toString();
                                Document doc1 = Jsoup.parse(firstVal);
                                Element aTag = doc1.select("td.fund_name a").first();

                                // Check if the <a> tag exists
                                if (aTag != null) {
                                    // Get the value of the 'href' attribute
                                    String hrefValue = aTag.attr("href");
                                    System.out.println("Href Value: " + hrefValue);
                                    Pattern pattern = Pattern.compile("/([^/]+)$");

                                    // Create a matcher with the link
                                    Matcher matcher = pattern.matcher(hrefValue);

                                    // Find the last occurrence of the pattern in the link
                                    if (matcher.find()) {
                                        // Extract the captured group (ine031a07840 in this case)
                                        String extractedCode = matcher.group(1);
                                        System.out.println("Extracted Code: " + extractedCode);
                                    } else {
                                        System.out.println("Code not found in the link.");
                                    }
                                } else {
                                    System.out.println("No <a> tag found.");
                                }
                            } else {
                                System.out.println("Row doesn't have enough cells.");
                            }
                            
                        }
                    } else {
                        System.out.println("Table not found.");
                    }

                    // Check if the table row exists
                    if (tableRow != null) {
                        // Do something with the table row, e.g., print its HTML content
                       // System.out.println("Table Row HTML: " + tableRow.outerHtml());
                    } else {
                        System.out.println("Table Row not found.");
                    }
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    

    @Scheduled(fixedRate = 1000) // 30 seconds in milliseconds
    @Override
    public void run(String... args) {
        // Your function here
        //https://www.thefixedincome.com/productlistajax?from_yield=&to_yield=&from_maturity=0&to_maturity=40&list_type=grad&product_type=1&page=2&search=&yieldSort=&url_filter=&maturityYearmax=40
        List<Bond> bonds = new ArrayList<>();


        bonds.add(new Bond("A1", "Company A", 750, 16));
        bonds.add(new Bond("B4", "Company B", 680, 21));
        bonds.add(new Bond("B1", "Company C", 800, 73));
        for(int i=0;i<bonds.size();i++){
            getUsersByXIRR("ine00qs24019");
        }
    //    getAllAlerts();
        //parseHtmlToBonds();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        //scheduler.scheduleAtFixedRate(()-> func(), 0L, 1000L,TimeUnit.MILLISECONDS); 
        //func();  
    }

}
