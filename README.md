# functionVision
## 功能说明
```
思考: Java8 引入了函数式编程，函数式编程有什么优点？
1. 使用泛型类型参数作为入参，使接收的对象的范围可以是Object级别的，即可以接收任何类型的数据。
2. 核心要点
a) 使用@FunctionInterface注解，帮助编译器检查Lambda语法
b) 使用泛型类型参数定义了入参和出参
c) 将函数的实现交给开发者，理论上有无限种实现，对于消解重复有很大的作用
3. 使用场景
在Java的流中广泛使用

本工程针对原生的java.util.function 的43种算子进行测试。

```
