package erp.partner;

import erp.general.DataSavingException;
import erp.general.DuplicatedDataException;
import erp.partner.dto.UpdatePartnerCommand;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PartnerService {

    private PartnerRepository partnerRepository;

    private ModelMapper modelMapper;

    public PartnerDTO createPartner(CreatePartnerCommand command) {
        Partner partner = new Partner(command.getName(), command.getAddress(), command.getTaxNo(), command.getIbans());
        try {
            partnerRepository.save(partner);
        } catch (DataIntegrityViolationException dive) {
            String message = dive.getRootCause().getMessage();
            if (message.startsWith("Duplicate entry")) {
                throw new DuplicatedDataException(message);
            }
            throw new DataSavingException(message);
        }
        return modelMapper.map(partner, PartnerDTO.class);
    }

    public PartnerDTO findPartnerById(String id) {
        Partner partner = partnerRepository.findById(id).orElseThrow(() -> new PartnerNotFoundException(id));
        return modelMapper.map(partner, PartnerDTO.class);
    }

    @Transactional
    public PartnerDTO addIban(String id, AddIbanCommand command) {
        Partner partner = partnerRepository.findById(id).orElseThrow(() -> new PartnerNotFoundException(id));
        partner.addIban(command.getIban());
        return modelMapper.map(partner, PartnerDTO.class);
    }

    @Transactional
    public PartnerDTO updatePartner(String id, UpdatePartnerCommand command) {
        Partner partner = partnerRepository.findById(id).orElseThrow(() -> new PartnerNotFoundException(id));
        partner.update(command);
        return modelMapper.map(partner, PartnerDTO.class);
    }

    public List<PartnerDTO> listPartnersByDifferentParams(Map<String, String> params) {
        List<Partner> partners = new ArrayList<>();
        Type targetListType = new TypeToken<List<PartnerDTO>>() {}.getType();
        if (params == null || params.isEmpty()) {
            partners = partnerRepository.findAll();
            return modelMapper.map(partners, targetListType);
        }
        if (params.containsKey("nameLike")) {
            partners = partnerRepository.findAllByNameLike("%" + params.get("nameLike") + "%");
        } else if (params.containsKey("iban")){
            partners = partnerRepository.findPartnerByIbansIsContaining("%" + params.get("iban") + "%");
        } else if (params.containsKey("taxNo")){
            partners = partnerRepository.findAllByTaxNo(params.get("taxNo"));
        }
        return modelMapper.map(partners, targetListType);
    }

    public void deletePartnerById(String id) {
        partnerRepository.deleteById(id);
    }
}
