package fa.training.restful.repositories;

import fa.training.restful.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByManufacturer(String manufacturer);
}
