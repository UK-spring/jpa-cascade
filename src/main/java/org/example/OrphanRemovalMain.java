package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.example.entity.Category;
import org.example.entity.Product;

public class OrphanRemovalMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("entity");

        EntityManager em = emf.createEntityManager();

        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        try {

            Category category = new Category("food");

            Product product1 = new Product("pizza");
            Product product2 = new Product("kimchi");
            category.addProduct(product1);
            category.addProduct(product2);

            em.persist(category);
            // cascade 제거
//            em.persist(product1);
//            em.persist(product2);

            em.flush();
            em.clear();

            // 고아 객체 삭제
            Category findCategory = em.find(Category.class, category.getId());
            findCategory.getProductList().remove(0);

            // cascade 제거
//            Category findCategory = em.find(Category.class, category.getId());
//            em.remove(findCategory);


            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
