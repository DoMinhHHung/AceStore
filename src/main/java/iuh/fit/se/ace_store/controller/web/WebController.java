package iuh.fit.se.ace_store.controller.web;

import iuh.fit.se.ace_store.entity.Product;
import iuh.fit.se.ace_store.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final ProductRepository productRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("pageTitle", "Trang chủ - Ace Store");
        return "index";
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("pageTitle", "Danh sách sản phẩm");
        return "products";
    }

    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        productRepository.findById(id).ifPresent(p -> model.addAttribute("product", p));
        model.addAttribute("pageTitle", "Chi tiết sản phẩm");
        return "product-details";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
}