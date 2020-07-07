package fa.training.restful.converter;

import fa.training.restful.dto.ProductDTO;
import fa.training.restful.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter {
    public ProductDTO toDto(Product entity) {
        ProductDTO result = new ProductDTO();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setCond(entity.getCond());
        result.setDescription(entity.getDescription());
        result.setImage(entity.getImage());
        result.setPrice(entity.getPrice());
        result.setCategoryCode(entity.getCategory().getCode());
        result.setManufacturerCode(entity.getStock().getManufacturer());
        result.setStockQuantity(entity.getStock().getQuantity());
        return result;
    }
    //convert DTO > entity, đổ vào entity mới
    public Product toEntity(ProductDTO dto) {
        Product result = new Product();
        result.setName(dto.getName());
        result.setCond(dto.getCond());
        result.setDescription(dto.getDescription());
        result.setImage(dto.getImage());
        result.setPrice(dto.getPrice());
        return result;
    }
}
