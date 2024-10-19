package com.future.function.vision;

import org.junit.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class CollectorTestMain {

    class User {
        private String name;
        private int age;
        private LocalDate registrationDate;

        public User(String name, int age, LocalDate registrationDate) {
            this.name = name;
            this.age = age;
            this.registrationDate = registrationDate;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public LocalDate getRegistrationDate() {
            return registrationDate;
        }

        @Override
        public String toString() {
            return String.format("(%s,%d,%s)", name, age, registrationDate);
        }
    }

    class Product {
        private String name;
        private String category;
        private double sales;

        public Product(String name, String category, double sales) {
            this.name = name;
            this.category = category;
            this.sales = sales;
        }

        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }

        public double getSales() {
            return sales;
        }

        @Override
        public String toString() {
            return String.format("(%s,%s,%.2f)", name, category, sales);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, category);
        }

        @Override
        public boolean equals(Object other) {
            if(other == null || this.getClass() != other.getClass()) {
                return false;
            }

            if(this == other) {
                return true;
            }

            Product p = (Product) other;
            return this.getName().equals(p.getName()) && this.getCategory().equals(((Product) other).getCategory());
        }
    }

    class AgeGroupSummary {
        private int age;
        private User latestUser;
        private long totalUsers;

        public AgeGroupSummary(int age, User latestUser, long totalUsers) {
            this.age = age;
            this.latestUser = latestUser;
            this.totalUsers = totalUsers;
        }

        @Override
        public String toString() {
            return "AgeGroupSummary{" +
                    "age=" + age +
                    ", latestUser=" + latestUser +
                    ", totalUsers=" + totalUsers +
                    '}';
        }
    }


    @Test
    public void testX() {
        List<Product> productList = genProduct().get();
        System.out.println("Initial:\n" + productList);
        //选取sales大于1000.00的产品
        List<Product> c1 = productList.stream().filter(p -> p.sales > 1000.00).collect(Collectors.toList());
        System.out.println("==> c1:\n"+ c1);
        //选取sales大于500.00的产品,按产品名称降序
        List<Product> c2 = productList.stream().filter(p -> p.sales > 500.00)
                .sorted(Comparator.comparing(Product::getName, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        System.out.println("==> c2:\n" + c2);
        //转成set(set会对对象去重)
        Set<Product> c3 = productList.stream().filter(p -> p.sales > 200).collect(Collectors.toSet());
        System.out.println("==> c3:\n" + c3);
        String c4 = productList.stream().map(Product::getName).collect(Collectors.joining());
        System.out.println("==> c4:\n" + c4);
        String c5 = productList.stream().map(Product::getName).collect(Collectors.joining("-"));
        System.out.println("==> c5:\n" + c5);
        String c6 = productList.stream().map(Product::getName).collect(Collectors.joining("#", "pre-", "$"));
        System.out.println("==> c6:\n" + c6);
        productList.stream().collect(Collectors.mapping(Product::getName, Collectors.toList()));
        String c7 = productList.stream().collect(Collectors.collectingAndThen(Collectors.summarizingDouble(Product::getSales), total -> String.format("Total Sales:%s", total)));
        System.out.println("==> c7:\n" + c7);
        long c8 = productList.stream().filter(p -> p.sales > 500.00).collect(Collectors.counting());
        System.out.println("==> c8:\n" + c8);
        Optional<Product> c9 = productList.stream().filter(p-> p.sales > 500.00).collect(Collectors.minBy(Comparator.comparing(Product::getName, Comparator.comparingInt(String::length)).thenComparing(Product::getSales, Comparator.reverseOrder())));
        System.out.println("==> c9:\n" + c9);
        Optional<Product> c10 = productList.stream().filter(p-> p.sales > 500.00).collect(Collectors.maxBy(Comparator.comparing(Product::getName, Comparator.comparingInt(String::length)).thenComparing(Product::getSales)));
        System.out.println("==> c10:\n" + c10);
        int c11 = productList.stream().filter(p-> p.sales > 500.00).collect(Collectors.summingInt(p -> (int) p.getSales()));
        System.out.println("==> c11:\n" + c11);
        double c12 = productList.stream().filter(p-> p.sales > 500.00).collect(Collectors.summingDouble(Product::getSales));
        System.out.println("==> c12:\n" + c12);
        double c13 = productList.stream().filter(p -> p.sales > 500.00).collect(Collectors.averagingDouble(Product::getSales));
        System.out.println("==> c13:\n" + c13);
        double c14 = productList.stream().filter(p -> p.sales > 500.00).collect(Collectors.reducing(0.00, Product::getSales, (s1, s2) -> s1 + s2));
        double c15 = productList.stream().filter(p -> p.sales > 500.00).collect(Collectors.reducing(0.00, Product::getSales, Double::sum));
        System.out.println("==> c14、c15:\n" + c14 + "," + c15);
        Optional<Product> c16 = productList.stream().filter(p -> p.sales > 500.00).collect(Collectors.reducing(BinaryOperator.maxBy(Comparator.comparing(Product::getSales))));
        System.out.println("==> c16:\n" + c16);
        double c17 = productList.stream().filter(p -> p.sales > 500.00).map(Product::getSales).collect(Collectors.reducing(0.00, (p1,p2)-> p1 + p2));
        System.out.println("==> c17:\n"+ c17);
        Object c18 = productList.stream().collect(Collectors.groupingBy(Product::getCategory));
        System.out.println("==> c18:\n" + c18);
        Map<String,Double> c19 = productList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble(Product::getSales)));
        System.out.println("==> c19:\n" + c19);
        Map<String,Double> c20 = productList.stream().collect(Collectors.groupingBy(Product::getCategory,TreeMap::new, Collectors.summingDouble(Product::getSales)));
        System.out.println("==> c20:\n" + c20);
        Object c21 = productList.parallelStream().collect(Collectors.groupingByConcurrent(Product::getCategory, Collectors.summarizingDouble(Product::getSales)));
        System.out.println("==> c21:\n" + c21);
        Object c22 = productList.parallelStream().collect(Collectors.groupingByConcurrent(Product::getCategory));
        System.out.println("==> c22:\n" + c22);
        Object c23 = productList.parallelStream().collect(Collectors.groupingByConcurrent(Product::getCategory, ConcurrentHashMap::new, Collectors.summingDouble(Product::getSales)));
        System.out.println("==> c23:\n" + c23);
        Map<Boolean, List<Product>> c24 = productList.stream().collect(Collectors.partitioningBy(p -> p.getSales() > 500.00));
        System.out.println("==> c24:\n" + c24);
        Object c25 = productList.stream().collect(Collectors.partitioningBy(p -> p.getSales() > 500.00, Collectors.summarizingDouble(Product::getSales)));
        System.out.println("==> c25:\n" + c25);
        Object c26 = productList.stream().distinct().collect(Collectors.toMap(Product::getName, Product::getSales));
        System.out.println("==> c26:\n" + c26);
        Object c27 = productList.stream().collect(Collectors.toMap(Product::getName, Product::getSales, BinaryOperator.maxBy(Comparator.comparingDouble(d -> d * 100.0))));
        System.out.println("==> c27:\n" + c27);
        Object c28 = productList.stream().collect(Collectors.toMap(Product::getName, Product::getSales, Double::sum, TreeMap::new));
        System.out.println("==> c28:\n" + c28);
        Object c29 = productList.stream().distinct().collect(Collectors.toConcurrentMap(Product::getName, Product::getSales));
        System.out.println("==> c29:\n" + c29);
        Object c30 = productList.stream().collect(Collectors.toConcurrentMap(Product::getName, Product::getSales, (k,v) -> {
            System.out.println(String.format("conflict k:%s, v:%s", k, v));
            return v + 300.00;
        }));
        System.out.println("==> c30:\n" + c30);
        Object c31 = productList.stream().collect(Collectors.toConcurrentMap(Product::getName, Product::getSales, (p1,p2) -> p1 > p2 ? p1:p2, ConcurrentHashMap::new));
        System.out.println("==> c31:\n" + c31);
        double c32 = productList.stream().collect(Collectors.summingDouble(Product::getSales));
        System.out.println("==> c32:\n" + c32);
    }

    @Test
    public void test0() {
        System.out.println("hi");
    }

    @Test
    public void test1() {
        List<User> userList = genUser().get();
        System.out.println("==> Initial:\n" + userList);
        //1.按年龄分组用户，并计算每个年龄组的用户数量
        Map<Integer, Long> c1 = userList.stream().collect(Collectors.groupingBy(User::getAge, Collectors.counting()));
        System.out.println("==> c1:\n" + c1);

        //2.按注册日期分组用户，并将用户姓名以逗号分隔的形式连接在一起
        Map<LocalDate, String> c2 = userList.stream()
                .collect(Collectors.groupingBy(User::getRegistrationDate, Collectors.mapping(User::getName, Collectors.joining(","))));
        System.out.println("==> c2:\n" + c2);
        //3.计算每个年龄组的用户平均年龄
        Map<Integer, Double> c3 = userList.stream().collect(Collectors.groupingBy(User::getAge, Collectors.averagingInt(User::getAge)));
        System.out.println("==> c3:\n" + c3);
        //4.获取每个年龄组的最新注册用户
        Map<Integer, Optional<User>> c4 = userList.stream().collect(Collectors.groupingBy(User::getAge, Collectors.maxBy(Comparator.comparing(User::getRegistrationDate))));
        System.out.println("==> c4:\n" + c4);
    }

    @Test
    public void test2() {
        List<Product> productList = genProduct().get();
        System.out.println("==> Initial:\n" + productList);

        //1.按分类汇总销售总额
        Map<String, Double> c1 = productList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.averagingDouble(Product::getSales)));
        System.out.println("==> c1:\n" + c1);

        //2.找出每个分类中销售最高的产品
        Map<String,Optional<Product>> c2 = productList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.maxBy(Comparator.comparing(Product::getSales))));
        System.out.println("==> c2:\n" + c2);
        //3.按分类将产品名称连接起来
        Map<String, String> c3 = productList.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.mapping(Product::getName, Collectors.joining(","))));
        System.out.println("==> c3:\n" + c3);
    }

    @Test
    public void test3() {
        //按年龄分组选取注册时间最晚的人及该组的人员总数
        List<User> userList = genUser().get();
        System.out.println("==>Initial:\n" + userList);
        List<AgeGroupSummary> c1 = userList.stream()
                .collect(Collectors.groupingBy(User::getAge))
                .entrySet()
                .stream()
                .map(entry -> {
                    int age = entry.getKey();
                    List<User> users = entry.getValue();
                    User lastUser = users.stream().max(Comparator.comparing(User::getRegistrationDate)).orElse(null);
                    int size = users.size();
                    return new AgeGroupSummary(age, lastUser, size);
                }).collect(Collectors.toList());
        System.out.println("==>c1:\n" + c1);
    }

    //用户生成
    private Supplier<List<User>> genUser() {
        return () -> Arrays.asList(
                new User("Alice", 30, LocalDate.of(2022, 1, 15)),
                new User("Bob", 25, LocalDate.of(2022, 3, 20)),
                new User("Charlie", 30, LocalDate.of(2021, 12, 10)),
                new User("David", 25, LocalDate.of(2022, 5, 30)),
                new User("Eve", 35, LocalDate.of(2022, 1, 20)),
                new User("Mark", 30, LocalDate.of(2022, 1, 15))
        );
    }

    //产品生成
    private Supplier<List<Product>> genProduct() {
        return () -> Arrays.asList(
                new Product("Laptop", "Electronics", 1200.00),
                new Product("Smartphone", "Electronics", 800.00),
                new Product("Tablet", "Electronics", 600.00),
                new Product("Chair", "Furniture", 150.00),
                new Product("Desk", "Furniture", 300.00),
                new Product("Laptop", "Electronics", 1300.00)
        );
    }
}
