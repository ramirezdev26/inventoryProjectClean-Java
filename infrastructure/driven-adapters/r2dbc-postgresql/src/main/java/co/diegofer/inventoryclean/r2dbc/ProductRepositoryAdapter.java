package co.diegofer.inventoryclean.r2dbc;

import co.diegofer.inventoryclean.model.commands.RegisterFinalCustomerSaleCommand.ProductSale;
import co.diegofer.inventoryclean.model.commands.custom.ProductToAdd;
import co.diegofer.inventoryclean.model.product.Product;
import co.diegofer.inventoryclean.model.product.gateways.ProductRepository;
import co.diegofer.inventoryclean.r2dbc.data.ProductData;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final ObjectMapper mapper;

    private final DatabaseClient dbClient;

    private final ProductRepositoryR2dbc productRepository;

    @Override
    public Mono<Product> saveAProduct(Product product) {
        ProductData productData = mapper.map(product, ProductData.class);
        dbClient.sql("INSERT INTO Product(id, name, description, inventory_stock, price, category, branch_id) VALUES(:id, :name, :description, :inventoryStock, :price, :category, :branchId)")
                .bind("id", product.getId())
                .bind("name", productData.getName())
                .bind("description", productData.getDescription())
                .bind("inventoryStock", productData.getInventoryStock())
                .bind("price", productData.getPrice())
                .bind("category", productData.getCategory())
                .bind("branchId", productData.getBranchId())
                .fetch()
                .one()
                .subscribe();
        return Mono.just(product);
    }

    @Override
    public Flux<Product> findProductsByBranch(String branchId) {
        return productRepository.findByBranchId(branchId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Branch not found")))
                .map(product -> mapper.map(product, Product.class));
    }

    @Override
    public Mono<List<ProductToAdd>> addStock(List<ProductToAdd> productsToAdd) {
        List<ProductToAdd> productsAddedList = new ArrayList<>();

        for(ProductToAdd productRequested: productsToAdd) {
            productRepository.findById(productRequested.getProductId())
                    .switchIfEmpty(Mono.error(new IllegalArgumentException("Product with id: " + productRequested.getProductId() + " was not found")))
                    .flatMap(product -> {
                        product.setInventoryStock(product.getInventoryStock()+productRequested.getQuantity());
                        productsAddedList.add(productRequested);
                        return productRepository.save(product);
                    }).map(product -> mapper.map(product, Product.class)).subscribe();

        }

        return Mono.just(productsAddedList);    }


    @Override
    public Mono<List<ProductSale>> reduceStock(List<ProductSale> productsRequested) {
        List<ProductSale> productsStockReducedList = new ArrayList<>();

        for(ProductSale productRequested: productsRequested) {
             productRepository
                    .findById(productRequested.getId())
                    .switchIfEmpty(Mono.error(new Throwable("There is no enough stock")))
                    .flatMap(product -> {
                        if (product.getInventoryStock() >= productRequested.getQuantity()) {
                            product.setInventoryStock(product.getInventoryStock() - productRequested.getQuantity());
                            productsStockReducedList.add(productRequested);
                            return productRepository.save(product);
                        } else return Mono.error(new Throwable("There is no enough stock"));
                    }).subscribe();

        }
        return Mono.just(productsStockReducedList);
    }
}
