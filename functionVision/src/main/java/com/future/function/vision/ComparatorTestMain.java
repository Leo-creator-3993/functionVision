package com.future.function.vision;

import com.future.function.vision.tool.OtherTool;
import com.github.javafaker.Faker;
import org.junit.Test;

import java.util.*;
import java.util.function.Supplier;

public class ComparatorTestMain {

    @Test
    public void test0() {
        System.out.println("hi");
    }

    class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("(%s,%s)", name, age);
        }
    }

    class XProduct{
        private String name;
        private double price;
        private double rating;

        public XProduct(String name, double price, double rating) {
            this.name = name;
            this.price = price;
            this.rating = rating;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        @Override
        public String toString() {
            return String.format("(%s,%s,%s)", name, price, rating);
        }
    }

    @Test
    public void testX() {
        List<XProduct> xProductList = genXProduct().get();
        System.out.println("Initial:\n" + xProductList);
        //按价格升序
        xProductList.sort(Comparator.comparing(XProduct::getPrice, Comparator.naturalOrder()));
        System.out.println("priceOrder:\n" + xProductList);
        //价格升序,价格一样时按评分降序
        xProductList.sort(Comparator.comparing(XProduct::getPrice, Comparator.naturalOrder()).thenComparing(XProduct::getRating, Comparator.reverseOrder()));
        System.out.println("priceOrderRatingReservedOrder:\n" + xProductList);
        //按价格降序
        xProductList.sort(Comparator.comparing(XProduct::getPrice).reversed());
        System.out.println("priceOrderReserved:\n" + xProductList);
        //按价格降序,价格一样时按评分升序
        xProductList.sort(Comparator.comparing(XProduct::getPrice, Comparator.reverseOrder()).thenComparing(XProduct::getRating, Comparator.naturalOrder()));
        System.out.println("priceReservedOrderRatingOrder:\n" + xProductList);
        //按价格降序,价格一样时按产品名称长度降序
        xProductList.sort(Comparator.comparing(XProduct::getPrice, Comparator.reverseOrder()).thenComparing(p-> p.getName().length(), Comparator.reverseOrder()));
        System.out.println("priceReservedOrderNameLenReservedOrder:\n"+  xProductList);
    }

    private Supplier<List<XProduct>> genXProduct() {
        return () -> Arrays.asList(
                new XProduct("phone", 999.99, 4.9),
                new XProduct("tv", 999.99, 4.2),
                new XProduct("computer", 1999.99, 4.1),
                new XProduct("car", 221999.99, 4.0),
                new XProduct("clothes", 100.33, 3.5),
                new XProduct("chair", 200.66, 2.5),
                new XProduct("rice", 200.66, 5.4),
                new XProduct("cpu", 32.33, 2.2)
                );
    }

    //简单排序
    @Test
    public void test1() {
        Faker faker = new Faker(Locale.SIMPLIFIED_CHINESE);
        Supplier<List<String>> generateName = () -> {
            List<String> nameList = new ArrayList<>();
            for(int i=0; i<5; i++){
                nameList.add(faker.name().fullName());
            };
            return nameList;
        };
        List<String> nameList = generateName.get();
        System.out.println(" ==> naturalOrder: ");
        nameList.sort(Comparator.naturalOrder());
        System.out.println(nameList);
        System.out.println(" ==> reverseOrder: ");
        Collections.sort(nameList, Comparator.reverseOrder());
        System.out.println(nameList);

        OtherTool.printSplitLine();
        java.text.Collator collector = java.text.Collator.getInstance();
        System.out.println(" ==> 拼音自然序和逆序: ");
        nameList.sort(collector);
        System.out.println(nameList);
        nameList.sort(collector.reversed());
        System.out.println(nameList);
    }

    //自定义对象和多重条件排序
    @Test
    public void test2() {
        List<Person> personList = gen().get();
        System.out.println("==> original: ");
        System.out.println(personList);
        System.out.println("==> nameOrder: ");
        personList.sort(Comparator.comparing(Person::getName));
        System.out.println(personList);
        System.out.println("==> reversedNameOrder: ");
        personList.sort(Comparator.comparing(Person::getName).reversed());
        System.out.println(personList);
        System.out.println("==> ageOrder: ");
        personList.sort(Comparator.comparing(Person::getAge));
        System.out.println(personList);
        System.out.println("==> reversedAgeOrder: ");
        personList.sort(Comparator.comparing(Person::getAge).reversed());
        System.out.println(personList);
        System.out.println("==> ageThenNameOrder: ");
        personList.sort(Comparator.comparing(Person::getAge).thenComparing(Person::getName));
        System.out.println(personList);
        System.out.println("==> ageThenNameOrderReversed: ");
        personList.sort(Comparator.comparing(Person::getAge).thenComparing(Person::getName).reversed());
        System.out.println(personList);
    }

    @Test
    public void test3() {
        List<Person> personList = gen().get();
        Comparator<Person> nameLenComparator = (p1,p2) -> p1.getName().length() - p2.getName().length();
        personList.sort(nameLenComparator);
        System.out.println(" ==> nameLenOrder:\n" + personList);
        personList.sort(nameLenComparator.reversed());
        System.out.println(" ==> nameLenOrderReserved:\n" + personList);
        personList.sort(nameLenComparator.thenComparing(Person::getAge));
        System.out.println(" ==> nameLenOrderThenAge:\n" + personList);
        personList.sort(nameLenComparator.thenComparing(Comparator.comparingInt(Person::getAge).reversed()));
        System.out.println(" ==> nameLenOrderThenAgeReserved:\n" + personList);
    }

    // 空值测试
    @Test
    public void test4() {
        List<Person> personList = gen().get();
        personList.add(null);
        personList.add(null);

        Comparator<Person> nameLenComparator = Comparator.comparingInt(p -> p.getName().length());
        personList.sort(Comparator.nullsFirst(Comparator.comparing(Person::getName)));
        System.out.println(" ==> nameOrderNullFirst:\n" + personList);
        personList.sort(Comparator.nullsLast(Comparator.comparing(Person::getName)));
        System.out.println(" ==> nameOrderNullLast:\n" + personList);
        personList.sort(Comparator.nullsFirst(nameLenComparator.thenComparing(Comparator.comparingInt(Person::getAge).reversed())));
        System.out.println(" ==> nameLenThenAgeReservedOrderNullFirst:\n" + personList);
        personList.sort(Comparator.nullsLast(nameLenComparator.thenComparing(Comparator.comparingInt(Person::getAge).reversed())));
        System.out.println(" ==> nameLenThenAgeReservedOrderNullLast:\n" + personList);
    }

    //生成person列表
    private Supplier<List<Person>> gen() {
        int count = 7;
        return () -> {
            List<Person> personList = new ArrayList<>();
            Random r = new Random();
            Faker faker = new Faker();
            int lastAge = -1;
            for(int i=0; i<count; i++) {
                String userName = faker.name().firstName();
                int age;
                //生成一些年龄重复数据
                if(i % 3 == 0 && lastAge != -1) {
                    age = lastAge;
                } else {
                    age = r.nextInt(40);
                    lastAge =age;
                }
                personList.add(new Person(userName, age));
            }
            return personList;
        };
    }
}
