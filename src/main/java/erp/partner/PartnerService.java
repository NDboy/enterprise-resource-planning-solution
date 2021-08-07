package erp.partner;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
public class PartnerService {

    private PartnerRepository partnerRepository;

    private ModelMapper modelMapper;

    public PartnerService(PartnerRepository partnerRepository, ModelMapper modelMapper) {
        this.partnerRepository = partnerRepository;
        this.modelMapper = modelMapper;
    }

    public PartnerDTO createPartner(CreatePartnerCommand command) {
        Partner partner = new Partner(command.getName(), command.getAddress(), command.getTaxNo());
        partnerRepository.save(partner);
        PartnerDTO partnerDTO = modelMapper.map(partner, PartnerDTO.class);
        return partnerDTO;
    }


    public List<PartnerDTO> listPartnersByName(Optional<String> name) {
        List<Partner> partners;
        if (name.isEmpty()) {
            partners = partnerRepository.findAll();
        } else {
            partners = partnerRepository.findAllByNameLike("%" + name.get() + "%");
        }
        Type targetListType = new TypeToken<List<PartnerDTO>>() {}.getType();

        return modelMapper.map(partners, targetListType);
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

    public PartnerDTO findPartnerByIban(String iban) {
        Partner partner = partnerRepository.findPartnerByIbansIsContaining("%" + iban + "%");
        return modelMapper.map(partner, PartnerDTO.class);
    }
}
