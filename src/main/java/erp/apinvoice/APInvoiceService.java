package erp.apinvoice;


import erp.partner.Partner;
import erp.partner.PartnerNotFoundException;
import erp.partner.PartnerRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class APInvoiceService {

    private ModelMapper modelMapper;

    private APInvoiceRepository apInvoiceRepository;

    private PartnerRepository partnerRepository;



    public APInvoiceDTO createAPInvoice(CreateAPInvoiceCommand command) {
        List<InvoiceItem> invoiceItems = command.getInvoiceItems()
                .stream()
                .map(ii -> new InvoiceItem(ii.getItemName(), ii.getNetPrice(), ii.getVatRate()))
                .collect(Collectors.toList());
        APInvoice apInvoice = new APInvoice(command.getInvNum(), command.getPaymentModeAndDates(), command.getInvoiceStatus(), invoiceItems);
        apInvoiceRepository.save(apInvoice);
        return modelMapper.map(apInvoice, APInvoiceDTO.class);
    }


    public List<APInvoiceDTO> listAPInvoicesOrFilteredByPartnerId(Optional<String> partnerid) {
        List<APInvoice> apInvoices;
        if (partnerid.isEmpty()) {
            apInvoices = apInvoiceRepository.findAll();
        } else {
            apInvoices = apInvoiceRepository.findAllByPartnerId(partnerid.get());
        }
        Type targetListType = new TypeToken<List<APInvoice>>() {}.getType();
        return modelMapper.map(apInvoices, targetListType);
    }

    public APInvoiceDTO findAPInvoiceById(String id) {
        APInvoice apInvoice = apInvoiceRepository.findById(id)
                .orElseThrow(() -> new APInvoiceNotFoundException(id));
        return modelMapper.map(apInvoice, APInvoiceDTO.class);
    }

    @Transactional
    public APInvoiceDTO addNewPartner(String id, AddNewPartnerCommand command) {
        APInvoice apInvoice = apInvoiceRepository.findById(id)
                .orElseThrow(() -> new APInvoiceNotFoundException(id));
        Partner partner = command.getPartner();
        if (partner.getId() != null) {
            partner = partnerRepository.findById(partner.getId()).orElseThrow(() -> new PartnerNotFoundException(id));
        } else {
            partnerRepository.save(partner);
        }
        apInvoice.setPartner(partner);
        return modelMapper.map(apInvoice, APInvoiceDTO.class);
    }

    public List<APInvoiceDTO> listAPInvoicesOrFilterByStatus(InvoiceStatus invoiceStatus) {
        List<APInvoice> apInvoices = apInvoiceRepository.findAllByInvoiceStatus(invoiceStatus);
        Type targetListType = new TypeToken<List<APInvoice>>() {}.getType();
        return modelMapper.map(apInvoices, targetListType);
    }

    @Transactional
    public APInvoiceDTO changeInvoiceStatus(String id, ChangeInvoiceStatusCommand command) {
        APInvoice apInvoice = apInvoiceRepository.findById(id)
                .orElseThrow(() -> new APInvoiceNotFoundException(id));
        apInvoice.setInvoiceStatus(command.getInvoiceStatus());
        return modelMapper.map(apInvoice, APInvoiceDTO.class);
    }



}

