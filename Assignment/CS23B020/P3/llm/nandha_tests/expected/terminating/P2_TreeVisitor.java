class TreeVisitor {
  public static void main(String[] a) {
    TV $1;
    $1 = new TV();
    $1.Start();
    System.out.println(0);
  }
}
class TV {
  public int Start() {
    Tree root;
    MyVisitor v;
    int $12;
    int $14;
    int $16;
    int $18;
    int $20;
    int $23;
    root = new Tree();
    root.Init(16);
    root.Print();
    System.out.println(100000000);
    root.Insert(8);
    root.Insert(24);
    root.Insert(4);
    root.Insert(12);
    root.Insert(20);
    root.Insert(28);
    root.Insert(14);
    root.Print();
    System.out.println(100000000);
    v = new MyVisitor();
    System.out.println(50000000);
    root.accept(v);
    System.out.println(100000000);
    $12 = root.Search(24);
    System.out.println($12);
    $14 = root.Search(12);
    System.out.println($14);
    $16 = root.Search(16);
    System.out.println($16);
    $18 = root.Search(50);
    System.out.println($18);
    $20 = root.Search(12);
    System.out.println($20);
    root.Delete(12);
    root.Print();
    $23 = root.Search(12);
    System.out.println($23);
    return 0;
  }
}
class Tree {
  Tree left;
  Tree right;
  int key;
  boolean has_left;
  boolean has_right;
  Tree my_null;
  public boolean Init(int v_key) {
    key = v_key;
    has_left = false;
    has_right = false;
    return true;
  }
  public boolean SetRight(Tree rn) {
    right = rn;
    return true;
  }
  public boolean SetLeft(Tree ln) {
    left = ln;
    return true;
  }
  public Tree GetRight() {
    Tree $0;
    $0 = right;
    return $0;
  }
  public Tree GetLeft() {
    Tree $0;
    $0 = left;
    return $0;
  }
  public int GetKey() {
    int $0;
    $0 = key;
    return $0;
  }
  public boolean SetKey(int v_key) {
    key = v_key;
    return true;
  }
  public boolean GetHas_Right() {
    boolean $0;
    $0 = has_right;
    return $0;
  }
  public boolean GetHas_Left() {
    boolean $0;
    $0 = has_left;
    return $0;
  }
  public boolean SetHas_Left(boolean val) {
    has_left = val;
    return true;
  }
  public boolean SetHas_Right(boolean val) {
    has_right = val;
    return true;
  }
  public boolean Compare(int num1, int num2) {
    boolean ntb;
    int nti;
    boolean $1;
    boolean $2;
    boolean $3;
    nti = num2 + 1;
    $1 = num1 < num2;
    if ($1) {
      ntb = false;
    } else {
      $3 = num1 < nti;
      $2 = !$3;
      if ($2) {
        ntb = false;
      } else {
        ntb = true;
      }
    }
    return ntb;
  }
  public boolean Insert(int v_key) {
    Tree new_node;
    Tree current_node;
    boolean cont;
    int key_aux;
    boolean $0;
    boolean $1;
    boolean $3;
    new_node = new Tree();
    new_node.Init(v_key);
    current_node = this;
    cont = true;
    while (cont) {
      key_aux = current_node.GetKey();
      $0 = v_key < key_aux;
      if ($0) {
        $1 = current_node.GetHas_Left();
        if ($1) {
          current_node = current_node.GetLeft();
        } else {
          cont = false;
          current_node.SetHas_Left(true);
          current_node.SetLeft(new_node);
        }
      } else {
        $3 = current_node.GetHas_Right();
        if ($3) {
          current_node = current_node.GetRight();
        } else {
          cont = false;
          current_node.SetHas_Right(true);
          current_node.SetRight(new_node);
        }
      }
    }
    return true;
  }
  public boolean Delete(int v_key) {
    Tree current_node;
    Tree parent_node;
    boolean cont;
    boolean found;
    boolean is_root;
    int key_aux;
    boolean $0;
    boolean $1;
    boolean $2;
    boolean $3;
    boolean $4;
    boolean $5;
    boolean $6;
    boolean $7;
    boolean $8;
    current_node = this;
    parent_node = this;
    cont = true;
    found = false;
    is_root = true;
    while (cont) {
      key_aux = current_node.GetKey();
      $0 = 12 < key_aux;
      if ($0) {
        $1 = current_node.GetHas_Left();
        if ($1) {
          parent_node = current_node;
          current_node = current_node.GetLeft();
        } else {
          cont = false;
        }
      } else {
        $2 = key_aux < 12;
        if ($2) {
          $3 = current_node.GetHas_Right();
          if ($3) {
            parent_node = current_node;
            current_node = current_node.GetRight();
          } else {
            cont = false;
          }
        } else {
          if (is_root) {
            $6 = current_node.GetHas_Right();
            $5 = !$6;
            $8 = current_node.GetHas_Left();
            $7 = !$8;
            if ($5) {
              $4 = $7;
            } else {
              $4 = false;
            }
            if ($4) {
            } else {
              this.Remove(parent_node, current_node);
            }
          } else {
            this.Remove(parent_node, current_node);
          }
          found = true;
          cont = false;
        }
      }
      is_root = false;
    }
    return found;
  }
  public boolean Remove(Tree p_node, Tree c_node) {
    int auxkey1;
    int auxkey2;
    boolean $0;
    boolean $1;
    Tree $2;
    boolean $3;
    Tree $4;
    Tree $6;
    $0 = c_node.GetHas_Left();
    if ($0) {
      this.RemoveLeft(p_node, c_node);
    } else {
      $1 = c_node.GetHas_Right();
      if ($1) {
        this.RemoveRight(p_node, c_node);
      } else {
        auxkey1 = c_node.GetKey();
        $2 = p_node.GetLeft();
        auxkey2 = $2.GetKey();
        $3 = this.Compare(auxkey1, auxkey2);
        if ($3) {
          $4 = my_null;
          p_node.SetLeft($4);
          p_node.SetHas_Left(false);
        } else {
          $6 = my_null;
          p_node.SetRight($6);
          p_node.SetHas_Right(false);
        }
      }
    }
    return true;
  }
  public boolean RemoveRight(Tree p_node, Tree c_node) {
    boolean $0;
    int $1;
    Tree $2;
    Tree $3;
    $0 = c_node.GetHas_Right();
    while ($0) {
      $2 = c_node.GetRight();
      $1 = $2.GetKey();
      c_node.SetKey($1);
      p_node = c_node;
      c_node = c_node.GetRight();
      $0 = c_node.GetHas_Right();
    }
    $3 = my_null;
    p_node.SetRight($3);
    p_node.SetHas_Right(false);
    return true;
  }
  public boolean RemoveLeft(Tree p_node, Tree c_node) {
    boolean $0;
    int $1;
    Tree $2;
    Tree $3;
    $0 = c_node.GetHas_Left();
    while ($0) {
      $2 = c_node.GetLeft();
      $1 = $2.GetKey();
      c_node.SetKey($1);
      p_node = c_node;
      c_node = c_node.GetLeft();
      $0 = c_node.GetHas_Left();
    }
    $3 = my_null;
    p_node.SetLeft($3);
    p_node.SetHas_Left(false);
    return true;
  }
  public int Search(int v_key) {
    Tree current_node;
    int ifound;
    boolean cont;
    int key_aux;
    boolean $0;
    boolean $1;
    boolean $2;
    boolean $3;
    current_node = this;
    cont = true;
    ifound = 0;
    while (cont) {
      key_aux = current_node.GetKey();
      $0 = v_key < key_aux;
      if ($0) {
        $1 = current_node.GetHas_Left();
        if ($1) {
          current_node = current_node.GetLeft();
        } else {
          cont = false;
        }
      } else {
        $2 = key_aux < v_key;
        if ($2) {
          $3 = current_node.GetHas_Right();
          if ($3) {
            current_node = current_node.GetRight();
          } else {
            cont = false;
          }
        } else {
          ifound = 1;
          cont = false;
        }
      }
    }
    return ifound;
  }
  public boolean Print() {
    Tree current_node;
    current_node = this;
    this.RecPrint(current_node);
    return true;
  }
  public boolean RecPrint(Tree node) {
    boolean $0;
    Tree $1;
    int $2;
    boolean $3;
    Tree $4;
    $0 = node.GetHas_Left();
    if ($0) {
      $1 = node.GetLeft();
      this.RecPrint($1);
    } else {
    }
    $2 = node.GetKey();
    System.out.println($2);
    $3 = node.GetHas_Right();
    if ($3) {
      $4 = node.GetRight();
      this.RecPrint($4);
    } else {
    }
    return true;
  }
  public int accept(Visitor v) {
    Tree $1;
    System.out.println(333);
    $1 = this;
    v.visit($1);
    return 0;
  }
}
class Visitor {
  Tree l;
  Tree r;
  public int visit(Tree n) {
    boolean $0;
    Tree $1;
    Visitor $2;
    boolean $3;
    Tree $4;
    Visitor $5;
    $0 = n.GetHas_Right();
    if ($0) {
      r = n.GetRight();
      $1 = r;
      $2 = this;
      $1.accept($2);
    } else {
    }
    $3 = n.GetHas_Left();
    if ($3) {
      l = n.GetLeft();
      $4 = l;
      $5 = this;
      $4.accept($5);
    } else {
    }
    return 0;
  }
}
class MyVisitor extends Visitor {
  public int visit(Tree n) {
    boolean $0;
    Tree $1;
    MyVisitor $2;
    int $3;
    boolean $4;
    Tree $5;
    MyVisitor $6;
    $0 = n.GetHas_Right();
    if ($0) {
      r = n.GetRight();
      $1 = r;
      $2 = this;
      $1.accept($2);
    } else {
    }
    $3 = n.GetKey();
    System.out.println($3);
    $4 = n.GetHas_Left();
    if ($4) {
      l = n.GetLeft();
      $5 = l;
      $6 = this;
      $5.accept($6);
    } else {
    }
    return 0;
  }
}
