package com.netcarat.service;

import com.netcarat.modal.NetcaratStock;
import com.netcarat.repository.NetcaratStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class NetcaratStockDataLoader implements CommandLineRunner {

    @Autowired
    private NetcaratStockRepository repository;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists to avoid duplicate loading
        if (repository.count() > 0) {
            System.out.println("NetcaratStock data already exists. Skipping data loading.");
            return;
        }

//        loadNetcaratStockData();
    }

    private void loadNetcaratStockData() {
        System.out.println("Loading NetcaratStock data...");

        // CSV data from the attached file
        String[] csvData = {
            "2502001,0.86,14,0.16,18KT,,,ER,EU50098ER,134.59,,,,,",
            "2502002,0.95,14,0.21,18KT,,,,EU50098ER,157.01,280,CASH,4,,,",
            "2502003,1.22,14,0.26,18KT,,,,EU50098ER,193.23,310,CASH,21,,,",
            "2502004,1.34,38,0.19,18KT,,,ER,SJR34541ER,180.68,,,,,",
            "2502005,1.42,38,0.35,18KT,,,ER,SJR34541ER,236.51,,,,,",
            "2502006,1.91,38,0.52,18KT,,,ER,SJR34541ER,326.86,,,,,",
            "2502007,2.46,36,0.92,18KT,,,,EUR50107ER,496.41,,,,,",
            "2502008,2.47,36,0.91,18KT,,,,EUR50107ER,494.16,,,,,",
            "2502009,2.55,44,0.19,18KT,,,,SJR34849ER,277.31,,,,,",
            "2502010,3.95,62,0.28,18KT,,,ER,SJR34849ER,425.12,,,,,",
            "2502011,1.50,14,0.07,18KT,,,ER,EUR50110ER,155.65,,,,,",
            "2502012,2.50,14,0.14,18KT,,,ER,EUR50110ER,257.5,,,,,",
            "2502013,4.77,14,0.42,18KT,,,ER,EUR50110ER,538.8,,,,,",
            "2502014,3.14,30,0.49,18KT,,,ER,EUR50123ER,421.02,,,,,",
            "2502015,1.92,30,0.16,18KT,,,ER,EUR50123ER,215.78,,,,,",
            "2502016,1.60,66,0.25,18KT,,,,EUR50127ER,219.23,300,CASH,17,,,",
            "2502017,2.91,66,0.81,18KT,,,,EUR50127ER,500.75,,,,,",
            "2502018,1.66,28,0.13,18KT,,,ER,SJR34849ER,186.55,,,,,",
            "2502019,2.36,34,0.30,18KT,,,,EUR50144ER,295.22,,,,,",
            "2502020,2.97,114,0.52,18KT,,,,EUR50170ER,415.79,,,,,",
            "2502021,2.92,18,0.14,18KT,,,,EUR50121ER,293.45,,,,,",
            "2502022,2.09,26,0.19,18KT,,,ER,NCR2019ER,237.94,,,,,",
            "2502023,1.42,36,0.10,18KT,,,ER,EUR50134ER,158.84,,,,,",
            "2502024,1.56,18,0.24,18KT,,,,EUR50126ER,228.06,410,CASH,4,12-Sep-2025,,",
            "2502025,3.14,18,0.95,18KT,,,,EUR50126ER,640.94,,,,,",
            "2502026,3.55,38,0.72,18KT,,,ER,SAM-5264-DCPS1019E,435.34,,,,,",
            "2502027,3.02,96,1.28,18KT,,,ER,SAM-5263-DCPS1034E,590.41,,RETURN TO SURAT,,,",
            "2502028,1.53,38,0.20,18KT,,,ER,EUR50159ER,214.59,,,,,",
            "2502029,4.37,106,1.22,18KT,,,,SAM-5266-DCPS1019E,575.45,692,CASH,2,5-Sep-2025,,",
            "2502030,2.75,46,0.75,18KT,,,ER,SAM-5265-DCPS1038E,432.49,,,,,",
            "2502031,2.30,14,0.07,18KT,0.87,B.SAPHIRE,ER,SJR34574ER,252.28,,,,,",
            "2502032,2.30,14,0.07,18KT,1.00,RUBBY,ER,SJR34574ER,257.31,,,,,",
            "2502033,2.28,14,0.07,18KT,0.71,EMRALD,ER,SJR34574ER,255.74,,,,,",
            "2502034,1.90,14,0.42,18KT,0.44,PERIDOT,ER,SJR34574ER,338.3,,,,,",
            "2502035,1.93,14,0.42,18KT,0.40,PERIDOT,ER,SJR34574ER,319.45,,,,,",
            "2502036,2.28,42,0.79,18KT,,,ER,SJR34542ER,440.61,,,,,",
            "2502037,2.31,42,0.42,18KT,0.52,O.SAPHIRE,ER,SJR34542ER,340.54,,,,,",
            "2502038,2.36,42,0.44,18KT,0.44,PERIDOT,ER,SJR34542ER,334.62,,,,,",
            "2502039,1.62,66,0.25,18KT,0.06,,ER,EUR50104ER,223.15,,,,,",
            "2502040,1.61,66,0.25,18KT,0.06,,,EUR50104ER,222.38,,,,,",
            "2502041,1.65,66,0.16,18KT,0.20,,ER,EUR50104ER,201.44,,,,,",
            "2502042,1.23,50,0.18,18KT,0.42,B.SAPHIRE,ER,EUR50108ER,192.11,,,,,",
            "2502043,1.25,50,0.16,18KT,0.34,RUBY,,EUR50108ER,177.61,240,CASH,18,,,",
            "2502044,1.18,30,0.10,18KT,0.34,RUBY,,EUR50161ER,161.43,,,,,",
            "2502045,1.79,34,0.20,18KT,0.83,EMRALD,,EUR50161ER,281.9,339,CASH,2,5-Sep-2025,,",
            "2502046,3.67,34,0.68,18KT,2.32,B.SAPHIRE,,EUR50161ER,740.44,,,,,",
            "2502047,1.99,58,0.23,18KT,0.59,B.SAPHIRE,,SJR34428ER,288.14,346,CASH,2,5-Sep-2025,,",
            "2502048,2.00,58,0.23,18KT,0.52,RUBY,,SJR34428ER,274.73,330,CASH,2,5-Sep-2025,,",
            "2502049,1.58,58,0.42,18KT,0.26,EMRALD,,SJR34428ER,284.73,342,CASH,2,5-Sep-2025,,",
            "2502050,6.29,49,0.18,18KT,4.60,SPINEL,,EUR50145ER,1232.29,,,,,",
            "2502051,1.62,57,0.16,18KT,,,,EUR50131RG,192.8,,,,,",
            "2502052,2.22,63,0.21,18KT,,,,SJR34719RG,255.28,459,CASH,13,16-Sep-2025,,",
            "2502053,2.21,17,0.18,18KT,,,,EUR50144RG,245.1,,RETURN TO SURAT,,,",
            "2502054,2.03,41,0.66,18KT,,,RG,EUR50168RG,378.82,,,,,",
            "2502055,2.15,14,0.06,18KT,,,RG,SJR34445RG,202.68,,,,,",
            "2502056,2.43,31,0.13,18KT,,,,NCR2010RG,248.4,,,,,",
            "2502057,1.51,5,0.16,18KT,,,,NCR2014RG,184.38,250,CASH,18,,,",
            "2502058,2.35,10,0.05,18KT,,,,EUR50121RG,216.7,,,,,",
            "2502059,2.27,25,0.36,18KT,,,RG,EUR50109RG,306.16,,,,,",
            "2502060,2.07,13,0.08,18KT,,,,EUR50133RG,202.05,,,,,",
            "2502061,2.68,31,0.13,18KT,,,,NCR2010RG,269.8,,,,,",
            "2502062,2.02,15,0.12,18KT,,,RG,EUR50123RG,210.2,,,,,",
            "2502063,3.14,15,0.32,18KT,,,RG,EUR50123RG,368.21,,,,,",
            "2502064,3.45,18,0.72,18KT,,,,EUR50107RG,519.02,,,,,",
            "2502065,3.38,18,0.72,18KT,,,RG,EUR50107RG,513.02,,,,,",
            "2502066,2.03,24,0.38,18KT,,,RG,SJR34457RG,291.83,,,,,",
            "2502067,1.08,14,0.21,18KT,,,RG,SJR34407RG,166.97,,,,,",
            "2502068,2.62,21,0.38,18KT,,,RG,SJR34542RG,342.33,,,,,",
            "2502069,1.98,34,0.63,18KT,,,,EUR50099RG,366.4,,,,,",
            "2502070,1.62,43,0.42,18KT,,,RG,EUR50099RG,273.58,,,,,",
            "2502071,1.34,49,0.27,18KT,,,,EUR50099RG,205.53,,,,,",
            "2502072,1.37,5,0.16,18KT,,,,EUR50148RG,173.65,,,,,",
            "2502073,2.70,20,0.08,18KT,,,RG,EUR50134RG,255.98,,,,,",
            "2502074,3.09,25,0.12,18KT,,,,EUR50134RG,301.79,,RETURN TO SURAT,,,",
            "2502075,1.60,20,0.10,18KT,,,RG,EUR50114RG,172.63,,,,,",
            "2502076,1.42,20,0.10,18KT,,,,EUR50114RG,158.84,215,CASH,18,,,",
            "2502077,2.09,27,0.26,18KT,,,RG,EUR50114RG,259.68,,,,,",
            "2502078,2.62,19,0.50,18KT,,,RG,SJR34541RG,379.62,,,,,",
            "2502079,2.74,19,0.37,18KT,,,RG,SJR34541RG,349.5,,,,,",
            "2502080,2.15,19,0.24,18KT,,,,SJR34541RG,258.61,,,,,",
            "2502081,1.45,5,0.34,18KT,,,RG,NCR2016RG,278.2,,,,,",
            "2502082,1.79,5,0.41,18KT,,,RG,NCR2016RG,355.25,,,,,",
            "2502083,1.22,5,0.21,18KT,,,RG,NCR2016RG,177.7,,,,,",
            "2502084,1.27,5,0.27,18KT,,,,NCR2016RG,200.17,,RETURN TO SURAT,,,",
            "2502085,1.26,5,0.16,18KT,,,RG,NCR2016RG,165.23,,,,,",
            "2502086,1.39,5,0.34,18KT,,,RG,NCR2016RG,231.11,,,,,",
            "2502087,1.73,3,0.10,18KT,,,RG,EUR50139RG,182.59,,,,,",
            "2502088,2.04,3,0.21,18KT,,,RG,EUR50139RG,266.12,,,,,",
            "2502089,2.32,3,0.40,18KT,,,RG,EUR50139RG,372.87,,,,,",
            "2502090,1.96,3,0.31,18KT,,,,EUR50139RG,304.2,,,,,",
            "2502091,2.89,29,0.69,18KT,,,,EUR50127RG,461.76,640,CASH,18,,,",
            "2502092,3.45,35,0.18,18KT,,,RG,SJR34849RG,351.25,,,,,",
            "2502093,2.65,25,0.12,18KT,,,,SJR34849RG,264.13,,,,,",
            "2502094,1.85,16,0.06,18KT,,,RG,SJR34849RG,179.35,,,,,",
            "2502095,2.32,29,0.20,18KT,,,,EUR50132RG,265.98,,,,,",
            "2502096,1.84,7,0.36,18KT,,,RG,EUR50098RG,292.79,,,,,",
            "2502097,2.17,25,0.25,18KT,,,,EUR50163RG,275.93,,,,,",
            "2502098,1.72,7,0.34,18KT,,,RG,EUR50098RG,268.89,,,,,",
            "2502099,1.99,7,0.27,18KT,,,RG,EUR50098RG,265.32,,,,,",
            "2502100,1.77,7,0.40,18KT,,,,NCR2009RG,331.11,595,CASH,4,12-Sep-2025,,"
        };

        int loadedCount = 0;
        for (String line : csvData) {
            try {
                NetcaratStock stock = parseCSVLine(line);
                if (stock != null) {
                    repository.save(stock);
                    loadedCount++;
                }
            } catch (Exception e) {
                System.err.println("Error parsing line: " + line + " - " + e.getMessage());
            }
        }

        System.out.println("Loaded " + loadedCount + " NetcaratStock records.");
    }

    private NetcaratStock parseCSVLine(String line) {
        String[] fields = line.split(",", -1); // -1 to keep empty strings
        
        if (fields.length < 15) {
            return null;
        }

        NetcaratStock stock = new NetcaratStock();
        
        // Parse ID
        stock.setId(Long.parseLong(fields[0].trim()));
        
        // Parse gross weight
        stock.setGrossWeight(parseDecimal(fields[1]));
        
        // Parse diamond pieces
        stock.setDiamondPieces(parseInteger(fields[2]));
        
        // Parse diamond weight
        stock.setDiamondWeight(parseDecimal(fields[3]));
        
        // Parse gold kt
        stock.setGoldKt(fields[4].trim().isEmpty() ? null : fields[4].trim());
        
        // Parse colour stone weight
        stock.setColourStoneWeight(parseDecimal(fields[5]));
        
        // Parse colour stone type
        stock.setColourStoneType(fields[6].trim().isEmpty() ? null : fields[6].trim());
        
        // Parse product category
        stock.setProductCatagory(fields[7].trim().isEmpty() ? null : fields[7].trim());
        
        // Parse design number
        stock.setDesignNumber(fields[8].trim().isEmpty() ? null : fields[8].trim());
        
        // Parse price
        stock.setPrice(parseDecimal(fields[9]));
        
        // Parse sold price
        stock.setSoldPrice(parseDecimal(fields[10]));
        
        // Parse payment type
        stock.setPaymentType(fields[11].trim().isEmpty() ? null : fields[11].trim());
        
        // Parse client id
        stock.setClientId(parseInteger(fields[12]));
        
        // Parse sell date
        stock.setSellDate(parseDate(fields[13]));
        
        // Parse description
        stock.setDescription(fields[14].trim().isEmpty() ? null : fields[14].trim());
        
        return stock;
    }

    private BigDecimal parseDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDate parseDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy");
            return LocalDate.parse(value.trim(), formatter);
        } catch (DateTimeParseException e) {
            try {
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                return LocalDate.parse(value.trim(), formatter2);
            } catch (DateTimeParseException e2) {
                return null;
            }
        }
    }
}