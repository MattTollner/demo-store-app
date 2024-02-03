//package com.mattcom.demostoreapp.service;
//
//
//import com.mattcom.demostoreapp.dao.*;
//
//import com.mattcom.demostoreapp.entity.*;
//import com.mattcom.demostoreapp.requestmodels.ProductCategoryRequest;
//import com.mattcom.demostoreapp.requestmodels.ProductRequest;
//import com.mattcom.demostoreapp.requestmodels.RoleRequest;
//import com.mattcom.demostoreapp.requestmodels.UserRequest;
//import jakarta.transaction.Transactional;
//
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@Transactional
//public class AdminService {
//    private ProductCategoryRepository productCategoryRepository;
//    private ImageDAO imageRepository;
//    private ProductRepository productRepository;
//    private UserRepository userRepository;
//    private RoleRepository roleRepository;
//
//    public AdminService(ProductCategoryRepository productCategoryRepository, ImageDAO imageRepository, ProductRepository productRepository, UserRepository userRepository, RoleRepository roleRepository) {
//        this.productCategoryRepository = productCategoryRepository;
//        this.imageRepository = imageRepository;
//        this.productRepository = productRepository;
//        this.userRepository = userRepository;
//        this.roleRepository = roleRepository;
//    }
//
//    public void addUser(UserRequest userRequest) {
//        User user = new User();
//        user.setCreateTime(LocalDate.now());
//        user.setEmail(userRequest.getEmail());
//        user.setFirstName(userRequest.getFirstName());
//        user.setLastName(userRequest.getLastName());
//        user.setPhoneNumber(userRequest.getPhoneNumber());
//        user.setPassword(userRequest.getPassword());
//        user.setRoles(userRequest.getRoles());
//
//        userRepository.save(user);
//    }
//
//    public void updateUser(UserRequest userRequest) throws Exception {
//        Optional<User> userOpt = userRepository.findById(userRequest.getId());
//        if (!userOpt.isPresent()) {
//            throw new Exception("User not found");
//        }
//
//        User user = userOpt.get();
//        user.setEmail(userRequest.getEmail());
//        user.setFirstName(userRequest.getFirstName());
//        user.setLastName(userRequest.getLastName());
//        user.setPhoneNumber(userRequest.getPhoneNumber());
//        user.setPassword(userRequest.getPassword());
//        user.setRoles(userRequest.getRoles());
//        for (Address2 address2 : userRequest.getAddresses()) {
//            address2.setUser(user);
//        }
//
//        user.setAddresses(userRequest.getAddresses());
//
//        userRepository.save(user);
//    }
//
//    public void deleteUser(Integer productId) throws Exception {
//        Optional<User> user = userRepository.findById(productId);
//        if (!user.isPresent()) {
//            throw new Exception("User not found");
//        }
//        User userToDel = user.get();
//        userToDel.setRoles(new HashSet<>());
//        userRepository.delete(userToDel);
//    }
//
//    public void addRole(RoleRequest roleRequest) {
//        Role role = new Role();
//        role.setRoleName(roleRequest.getRoleName());
//        roleRepository.save(role);
//    }
//
//    public void updateRole(RoleRequest roleRequest) throws Exception {
//        Optional<Role> roleOpt = roleRepository.findById(roleRequest.getId());
//        if (!roleOpt.isPresent()) {
//            throw new Exception("User not found");
//        }
//
//        Role role = roleOpt.get();
//        role.setRoleName(roleRequest.getRoleName());
//        roleRepository.save(role);
//    }
//
//    public void deleteRole(Integer roleId) throws Exception {
//        Optional<Role> role = roleRepository.findById(roleId);
//        if (!role.isPresent()) {
//            throw new Exception("Role not found");
//        }
//        Role roleToDel = role.get();
//        roleToDel.setUsers(new HashSet<>());
//        roleRepository.delete(roleToDel);
//    }
//
//    public void addProduct(ProductRequest productRequest) {
//        Product product = new Product();
//
//        Optional<ProductCategory> parent = productCategoryRepository.findById(productRequest.getProductCategoryId());
//        if(!parent.isPresent()){
//            throw new Error("Category not found");
//        }
//
//        product.setImages(productRequest.getImages());
//
//        product.setProductCategory(parent.get());
//        product.setProductName(productRequest.getProductName());
//        product.setProductDescription(productRequest.getProductDescription());
//        product.setPrice(productRequest.getPrice());
//        product.setStock(productRequest.getStock());
//        productRepository.save(product);
//    }
//
////    private Image addImage(Image image){
////        Image newImage = new Image();
////        newImage.setImgUrl(image.getImgUrl());
////        return imageRepository.save(newImage);
////    }
//
//
//
//    public void deleteProduct(Integer productId) throws Exception {
//        Optional<Product> product = productRepository.findById(productId);
//        if (!product.isPresent()) {
//            throw new Exception("Product not found");
//        }
//        productRepository.delete(product.get());
//    }
//
//    public void updateProduct(ProductRequest productRequest) throws Exception {
//        Optional<Product> productOpt = productRepository.findById(productRequest.getId());
//        if (!productOpt.isPresent()) {
//            throw new Exception("Product not found");
//        }
//
//        ProductCategory category = null;
//        if(productRequest.getProductCategoryId() != -1){
//            Optional<ProductCategory> categoryOpt = productCategoryRepository.findById(productRequest.getProductCategoryId());
//            if(!categoryOpt.isPresent()){
//                throw new Exception("Parent Category Not Found");
//            }
//            category = categoryOpt.get();
//        }
//
//        Product product = productOpt.get();
//        product.setProductCategory(category);
//        product.setPrice(productRequest.getPrice());
//        product.setProductName(productRequest.getProductName());
//        product.setStock(productRequest.getStock());
//        product.setProductDescription(productRequest.getProductDescription());
//        product.setImages(productRequest.getImages());
//        productRepository.save(product);
//    }
//
//    public void updateProductCategory(ProductCategoryRequest productCategoryRequest) throws Exception {
//        Optional<ProductCategory> categoryOpt = productCategoryRepository.findById(productCategoryRequest.getId());
//        if (!categoryOpt.isPresent()) {
//            throw new Exception("Category not found");
//        }
//
//        ProductCategory parent = null;
//        if(productCategoryRequest.getParentCategoryId() != -1){
//            Optional<ProductCategory> parentOpt = productCategoryRepository.findById(productCategoryRequest.getParentCategoryId());
//            if(!parentOpt.isPresent()){
//                throw new Exception("Parent Category Not Found");
//            }
//            parent = parentOpt.get();
//        }
//
//        ProductCategory category = categoryOpt.get();
//        category.setParentCategory(parent);
//        category.setCategoryName(productCategoryRequest.getCategoryName());
//        productCategoryRepository.save(category);
//    }
//
//
//    public void addProductCategory(ProductCategoryRequest productCategoryRequest) {
//        ProductCategory productCategory = new ProductCategory();
//
//        Optional<ProductCategory> parent = productCategoryRepository.findById(productCategoryRequest.getParentCategoryId());
//        if(parent.isPresent()){
//            productCategory.setParentCategory(parent.get());
//        }
//
//        productCategory.setCategoryName(productCategoryRequest.getCategoryName());
//        productCategoryRepository.save(productCategory);
//    }
//
//    public void addProductCategory2() {
//        List<ProductCategory> pc = productCategoryRepository.findAll();
//        System.out.println(pc.toString());
//    }
//
//    public void deleteProductCategory(Integer categoryId) throws Exception {
//        Optional<ProductCategory> category = productCategoryRepository.findById(categoryId);
//        if (!category.isPresent()) {
//            throw new Exception("Category not found");
//        }
//        productCategoryRepository.delete(category.get());
//    }
//
//    public List<User> getUsersAndRoles(){
//        return userRepository.getUsersAndRoles();
//    }
//
//    public User getUserAndRolesById(int id){
//        return userRepository.getUserAndRolesById(id);
//    }
//}
