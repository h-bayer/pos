package de.bayer.pharmacy.edgeposapi.device;

import org.springframework.stereotype.Service;

@Service
public class ScannerService {
    public String decodeBarcode(byte[] payload) {
        return new String(payload);
    }
}
