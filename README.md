# Smoothie - simple java dependency injection container

## Usage

```
Smoothie smoothie = new Smoothie()
smoothie.init("packageName");
Class obj = (Class)smoothie.get(Class);
obj.someMethod();
```
