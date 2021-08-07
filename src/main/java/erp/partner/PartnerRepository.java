package erp.partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner, String> {

    List<Partner> findAllByNameLike(String name);

    @Query("select distinct p from Partner p join fetch p.ibans i where i like :iban")
    Partner findPartnerByIbansIsContaining(String iban);


}
