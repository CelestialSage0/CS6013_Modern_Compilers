class LinkedList {
  public static void main(String[] a) {
    int $0;
    LL $1;
    $1 = new LL();
    $0 = $1.Start();
    System.out.println($0);
  }
}
class Element {
  int Age;
  int Salary;
  boolean Married;
  public boolean Init(int v_Age, int v_Salary, boolean v_Married) {
    boolean $0;
    Age = v_Age;
    Salary = v_Salary;
    Married = v_Married;
    $0 = true;
    return $0;
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
    int nt;
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
            nt = 0;
          }
        } else {
          $9 = other.GetMarried();
          if ($9) {
            ret_val = false;
          } else {
            nt = 0;
          }
        }
      }
    }
    return ret_val;
  }
  public boolean Compare(int num1, int num2) {
    boolean retval;
    int aux02;
    int $0;
    boolean $1;
    boolean $2;
    boolean $3;
    retval = false;
    $0 = 1;
    aux02 = num2 + $0;
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
    boolean $0;
    end = true;
    $0 = true;
    return $0;
  }
  public boolean InitNew(Element v_elem, List v_next, boolean v_end) {
    boolean $0;
    end = v_end;
    elem = v_elem;
    next = v_next;
    $0 = true;
    return $0;
  }
  public List Insert(Element new_elem) {
    boolean ret_val;
    List aux03;
    List aux02;
    boolean $0;
    aux03 = this;
    aux02 = new List();
    $0 = false;
    ret_val = aux02.InitNew(new_elem, aux03, $0);
    return aux02;
  }
  public boolean SetNext(List v_next) {
    boolean $0;
    next = v_next;
    $0 = true;
    return $0;
  }
  public List Delete(Element e) {
    List my_head;
    boolean ret_val;
    boolean aux05;
    List aux01;
    List prev;
    boolean var_end;
    Element var_elem;
    int aux04;
    int nt;
    int $0;
    int $1;
    boolean $2;
    boolean $3;
    boolean $4;
    boolean $5;
    boolean $6;
    int $7;
    int $8;
    int $9;
    int $10;
    List $11;
    int $12;
    int $13;
    int $14;
    boolean $15;
    boolean $16;
    boolean $17;
    my_head = this;
    ret_val = false;
    $0 = 0;
    $1 = 1;
    aux04 = $0 - $1;
    aux01 = this;
    prev = this;
    var_end = end;
    var_elem = elem;
    $3 = !var_end;
    $4 = !ret_val;
    if ($3) {
      $2 = $4;
    } else {
      $2 = false;
    }
    while ($2) {
      $5 = e.Equal(var_elem);
      if ($5) {
        ret_val = true;
        $7 = 0;
        $6 = aux04 < $7;
        if ($6) {
          my_head = aux01.GetNext();
        } else {
          $9 = 0;
          $10 = 555;
          $8 = $9 - $10;
          System.out.println($8);
          $11 = aux01.GetNext();
          aux05 = prev.SetNext($11);
          $13 = 0;
          $14 = 555;
          $12 = $13 - $14;
          System.out.println($12);
        }
      } else {
        nt = 0;
      }
      $15 = !ret_val;
      if ($15) {
        prev = aux01;
        aux01 = aux01.GetNext();
        var_end = aux01.GetEnd();
        var_elem = aux01.GetElem();
        aux04 = 1;
      } else {
        nt = 0;
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
    int nt;
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
        nt = 0;
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
    boolean $2;
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
    $2 = true;
    return $2;
  }
}
class LL {
  public int Start() {
    List head;
    List last_elem;
    boolean aux01;
    Element el01;
    Element el02;
    Element el03;
    int $0;
    int $1;
    boolean $2;
    int $3;
    int $4;
    int $5;
    boolean $6;
    int $7;
    int $8;
    int $9;
    boolean $10;
    int $11;
    int $12;
    boolean $13;
    int $14;
    int $15;
    int $16;
    int $17;
    int $18;
    boolean $19;
    int $20;
    int $21;
    int $22;
    int $23;
    last_elem = new List();
    aux01 = last_elem.Init();
    head = last_elem;
    aux01 = head.Init();
    aux01 = head.Print();
    el01 = new Element();
    $0 = 25;
    $1 = 37000;
    $2 = false;
    aux01 = el01.Init($0, $1, $2);
    head = head.Insert(el01);
    aux01 = head.Print();
    $3 = 10000000;
    System.out.println($3);
    el01 = new Element();
    $4 = 39;
    $5 = 42000;
    $6 = true;
    aux01 = el01.Init($4, $5, $6);
    el02 = el01;
    head = head.Insert(el01);
    aux01 = head.Print();
    $7 = 10000000;
    System.out.println($7);
    el01 = new Element();
    $8 = 22;
    $9 = 34000;
    $10 = false;
    aux01 = el01.Init($8, $9, $10);
    head = head.Insert(el01);
    aux01 = head.Print();
    el03 = new Element();
    $11 = 27;
    $12 = 34000;
    $13 = false;
    aux01 = el03.Init($11, $12, $13);
    $14 = head.Search(el02);
    System.out.println($14);
    $15 = head.Search(el03);
    System.out.println($15);
    $16 = 10000000;
    System.out.println($16);
    el01 = new Element();
    $17 = 28;
    $18 = 35000;
    $19 = false;
    aux01 = el01.Init($17, $18, $19);
    head = head.Insert(el01);
    aux01 = head.Print();
    $20 = 2220000;
    System.out.println($20);
    head = head.Delete(el02);
    aux01 = head.Print();
    $21 = 33300000;
    System.out.println($21);
    head = head.Delete(el01);
    aux01 = head.Print();
    $22 = 44440000;
    System.out.println($22);
    $23 = 0;
    return $23;
  }
}
