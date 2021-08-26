package erp.partner;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PartnerService {

    private PartnerRepository partnerRepository;

    private ModelMapper modelMapper;

    public PartnerDTO createPartner(CreatePartnerCommand command) {
        Partner partner = new Partner(command.getName(), command.getAddress(), command.getTaxNo());
        partnerRepository.save(partner);
        PartnerDTO partnerDTO = modelMapper.map(partner, PartnerDTO.class);
        return partnerDTO;
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
