class LinkedList {
  public static void main(String[] a) {
    LL $1;
    $1 = new LL();
    $1.Start();
    System.out.println(0);
  }
}
class Element {
  int Age;
  int Salary;
  boolean Married;
  public boolean Init(int v_Age, int v_Salary, boolean v_Married) {
    Age = v_Age;
    Salary = v_Salary;
    Married = v_Married;
    return true;
  }
  public int GetAge() {
    int $0;
    $0 = Age;
    return $0;
  }
  public int GetSalary() {
    int $0;
    $0 = Salary;
    return $0;
  }
  public boolean GetMarried() {
    boolean $0;
    $0 = Married;
    return $0;
  }
  public boolean Equal(Element other) {
    boolean ret_val;
    int aux01;
    int aux02;
    boolean $0;
    boolean $1;
    int $2;
    boolean $3;
    boolean $4;
    int $5;
    boolean $6;
    boolean $7;
    boolean $8;
    boolean $9;
    ret_val = true;
    aux01 = other.GetAge();
    $2 = Age;
    $1 = this.Compare(aux01, $2);
    $0 = !$1;
    if ($0) {
      ret_val = false;
    } else {
      aux02 = other.GetSalary();
      $5 = Salary;
      $4 = this.Compare(aux02, $5);
      $3 = !$4;
      if ($3) {
        ret_val = false;
      } else {
        $6 = Married;
        if ($6) {
          $8 = other.GetMarried();
          $7 = !$8;
          if ($7) {
            ret_val = false;
          } else {
          }
        } else {
          $9 = other.GetMarried();
          if ($9) {
            ret_val = false;
          } else {
          }
        }
      }
    }
    return ret_val;
  }
  public boolean Compare(int num1, int num2) {
    boolean retval;
    int aux02;
    boolean $1;
    boolean $2;
    boolean $3;
    aux02 = num2 + 1;
    $1 = num1 < num2;
    if ($1) {
      retval = false;
    } else {
      $3 = num1 < aux02;
      $2 = !$3;
      if ($2) {
        retval = false;
      } else {
        retval = true;
      }
    }
    return retval;
  }
}
class List {
  Element elem;
  List next;
  boolean end;
  public boolean Init() {
    end = true;
    return true;
  }
  public boolean InitNew(Element v_elem, List v_next, boolean v_end) {
    end = false;
    elem = v_elem;
    next = v_next;
    return true;
  }
  public List Insert(Element new_elem) {
    List aux03;
    List aux02;
    aux03 = this;
    aux02 = new List();
    aux02.InitNew(new_elem, aux03, false);
    return aux02;
  }
  public boolean SetNext(List v_next) {
    next = v_next;
    return true;
  }
  public List Delete(Element e) {
    List my_head;
    boolean ret_val;
    List aux01;
    List prev;
    boolean var_end;
    Element var_elem;
    int aux04;
    boolean $2;
    boolean $3;
    boolean $5;
    boolean $6;
    List $11;
    boolean $15;
    boolean $16;
    boolean $17;
    my_head = this;
    ret_val = false;
    aux04 = -1;
    aux01 = this;
    prev = this;
    var_end = end;
    var_elem = elem;
    $3 = !var_end;
    if ($3) {
      $2 = true;
    } else {
      $2 = false;
    }
    while ($2) {
      $5 = e.Equal(var_elem);
      if ($5) {
        ret_val = true;
        $6 = aux04 < 0;
        if ($6) {
          my_head = aux01.GetNext();
        } else {
          System.out.println(-555);
          $11 = aux01.GetNext();
          prev.SetNext($11);
          System.out.println(-555);
        }
      } else {
      }
      $15 = !ret_val;
      if ($15) {
        prev = aux01;
        aux01 = aux01.GetNext();
        var_end = aux01.GetEnd();
        var_elem = aux01.GetElem();
        aux04 = 1;
      } else {
      }
      $16 = !var_end;
      $17 = !ret_val;
      if ($16) {
        $2 = $17;
      } else {
        $2 = false;
      }
    }
    return my_head;
  }
  public int Search(Element e) {
    int int_ret_val;
    List aux01;
    Element var_elem;
    boolean var_end;
    boolean $0;
    boolean $1;
    int_ret_val = 0;
    aux01 = this;
    var_end = end;
    var_elem = elem;
    $0 = !var_end;
    while ($0) {
      $1 = e.Equal(var_elem);
      if ($1) {
        int_ret_val = 1;
      } else {
      }
      aux01 = aux01.GetNext();
      var_end = aux01.GetEnd();
      var_elem = aux01.GetElem();
      $0 = !var_end;
    }
    return int_ret_val;
  }
  public boolean GetEnd() {
    boolean $0;
    $0 = end;
    return $0;
  }
  public Element GetElem() {
    Element $0;
    $0 = elem;
    return $0;
  }
  public List GetNext() {
    List $0;
    $0 = next;
    return $0;
  }
  public boolean Print() {
    List aux01;
    boolean var_end;
    Element var_elem;
    boolean $0;
    int $1;
    aux01 = this;
    var_end = end;
    var_elem = elem;
    $0 = !var_end;
    while ($0) {
      $1 = var_elem.GetAge();
      System.out.println($1);
      aux01 = aux01.GetNext();
      var_end = aux01.GetEnd();
      var_elem = aux01.GetElem();
      $0 = !var_end;
    }
    return true;
  }
}
class LL {
  public int Start() {
    List head;
    List last_elem;
    Element el01;
    Element el02;
    Element el03;
    int $14;
    int $15;
    last_elem = new List();
    last_elem.Init();
    head = last_elem;
    head.Init();
    head.Print();
    el01 = new Element();
    el01.Init(25, 37000, false);
    head = head.Insert(el01);
    head.Print();
    System.out.println(10000000);
    el01 = new Element();
    el01.Init(39, 42000, true);
    el02 = el01;
    head = head.Insert(el01);
    head.Print();
    System.out.println(10000000);
    el01 = new Element();
    el01.Init(22, 34000, false);
    head = head.Insert(el01);
    head.Print();
    el03 = new Element();
    el03.Init(27, 34000, false);
    $14 = head.Search(el02);
    System.out.println($14);
    $15 = head.Search(el03);
    System.out.println($15);
    System.out.println(10000000);
    el01 = new Element();
    el01.Init(28, 35000, false);
    head = head.Insert(el01);
    head.Print();
    System.out.println(2220000);
    head = head.Delete(el02);
    head.Print();
    System.out.println(33300000);
    head = head.Delete(el01);
    head.Print();
    System.out.println(44440000);
    return 0;
  }
}
