package de.bayer.pharmacy.edgeposapi.device;

import org.springframework.stereotype.Service;

@Service
public class PaymentTerminalService {
    public boolean authorize(int cents) {
        return true;
    }
}
