package erp.partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner, String> {

    List<Partner> findAllByNameLike(String nameLike);

    @Query("select p from Partner p join fetch p.ibans i where i like :iban")
    List<Partner> findPartnerByIbansIsContaining(String iban);

    List<Partner> findAllByTaxNo(String taxNo);
}
