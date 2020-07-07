package fa.training.restful.controllers;

import fa.training.restful.converter.ProductConverter;
import fa.training.restful.dto.ProductDTO;
import fa.training.restful.entities.Category;
import fa.training.restful.entities.Product;
import fa.training.restful.entities.Stock;
import fa.training.restful.entities.User;
import fa.training.restful.exceptions.ResourceNotFoundException;
import fa.training.restful.repositories.CategoryRepository;
import fa.training.restful.repositories.ProductRepository;
import fa.training.restful.repositories.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private StockRepository stockRepository;

    @GetMapping("/list")
    public ProductDTO getAllProducts() {
        ProductDTO model = new ProductDTO();
        List<ProductDTO> listResult = new ArrayList<>();
        List<Product> listEntity = productRepository.findAll();
        for (Product item: listEntity) {
            ProductDTO dto = new ProductConverter().toDto(item);
            dto.setCategoryCode(item.getCategory().getCode());
            dto.setManufacturerCode(item.getStock().getManufacturer());
            dto.setStockQuantity(item.getStock().getQuantity());
            listResult.add(dto);
        }
        model.setListResult(listResult);
        return model;
    }

    @PostMapping("/add")
    public ProductDTO create(@Validated @RequestBody ProductDTO dto) {
        ProductDTO model = new ProductDTO();
        List<ProductDTO> listResult = new ArrayList<>();
        Category category = categoryRepository.findByCode(dto.getCategoryCode());
        Stock stock = stockRepository.findByManufacturer(dto.getManufacturerCode());
        if (category == null || stock == null) {
            return null;
        }
        stock.setQuantity(dto.getStockQuantity().intValue() + stock.getQuantity().intValue());
        for (int i = 0; i < dto.getStockQuantity().intValue(); i++) {
            Product product = new ProductConverter().toEntity(dto);
            product.setCategory(category);
            product.setStock(stock);
            Product result = productRepository.save(product);
            listResult.add(new ProductConverter().toDto(result));
        }
        model.setListResult(listResult);
        return model;
    }

    @DeleteMapping("/delete/{id}")
    public Map<String, Boolean> delete(@PathVariable(value = "id") Long productId) throws ResourceNotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("User not found on : "+ productId));
        Stock stock = stockRepository.findByManufacturer(product.getStock().getManufacturer());
        stock.setQuantity(stock.getQuantity()-1);
        productRepository.delete(product);
        Map<String, Boolean> responseMap = new HashMap<>();
        responseMap.put("deleted", Boolean.TRUE);
        return responseMap;
    }
}
