package smoothie;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Field;
import java.util.*;

class Smoothie {
  Map<Class<?>, Object> objects = new HashMap<>();

  public void init(String packageName) {
    this.findAllComponents(packageName);
    this.fillObjects();
  }

  public Object get(Class<?> clazz) {
    return objects.get(clazz);
  }

  private void findAllComponents(String packageName) {
    Reflections reflections = new Reflections(packageName, new SubTypesScanner(false)); 
    Set<Class<?>> allClasses = reflections.getSubTypesOf(Object.class);

    System.out.println(allClasses);

    for (Class<?> clazz : allClasses) {
      if (clazz.isAnnotationPresent(Component.class)) {
        try {
          objects.put(clazz, clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    System.out.println(this.objects);
  }

  private void fillObjects() {
    for (Map.Entry<Class<?>, Object> entry : this.objects.entrySet()) {
      Object object = entry.getValue();

      Field[] fields = object.getClass().getDeclaredFields();
      for (Field field : fields) {
        if (field.isAnnotationPresent(Autowired.class)) {
          Class<?> fieldClass = field.getType();
          Object fieldImplementation = this.objects.get(fieldClass);
          
          field.setAccessible(true);
          try {
            field.set(object, fieldImplementation);
          } catch (Exception e) {
            e.printStackTrace();
          }
          field.setAccessible(false);
        }
      }
    }
  }
}
