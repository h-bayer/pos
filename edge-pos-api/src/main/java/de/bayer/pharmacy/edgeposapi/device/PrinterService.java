package de.bayer.pharmacy.edgeposapi.device;

import org.springframework.stereotype.Service;

@Service
public class PrinterService {
    public void printReceipt(String text) {
        System.out.println("[PRINTER] " + text);
    }
}
