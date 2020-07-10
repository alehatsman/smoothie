package smoothie;

@Component
class Foo {
  @Autowired
  private Bar bar;

  public void hello() {
    this.bar.bar();
  }
}
