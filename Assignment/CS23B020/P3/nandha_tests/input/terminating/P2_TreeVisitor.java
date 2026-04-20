class TreeVisitor {
  public static void main(String[] a) {
    int $0;
    TV $1;
    $1 = new TV();
    $0 = $1.Start();
    System.out.println($0);
  }
}
class TV {
  public int Start() {
    Tree root;
    boolean ntb;
    int nti;
    MyVisitor v;
    int $0;
    int $1;
    int $2;
    int $3;
    int $4;
    int $5;
    int $6;
    int $7;
    int $8;
    int $9;
    int $10;
    int $11;
    int $12;
    int $13;
    int $14;
    int $15;
    int $16;
    int $17;
    int $18;
    int $19;
    int $20;
    int $21;
    int $22;
    int $23;
    int $24;
    int $25;
    root = new Tree();
    $0 = 16;
    ntb = root.Init($0);
    ntb = root.Print();
    $1 = 100000000;
    System.out.println($1);
    $2 = 8;
    ntb = root.Insert($2);
    $3 = 24;
    ntb = root.Insert($3);
    $4 = 4;
    ntb = root.Insert($4);
    $5 = 12;
    ntb = root.Insert($5);
    $6 = 20;
    ntb = root.Insert($6);
    $7 = 28;
    ntb = root.Insert($7);
    $8 = 14;
    ntb = root.Insert($8);
    ntb = root.Print();
    $9 = 100000000;
    System.out.println($9);
    v = new MyVisitor();
    $10 = 50000000;
    System.out.println($10);
    nti = root.accept(v);
    $11 = 100000000;
    System.out.println($11);
    $13 = 24;
    $12 = root.Search($13);
    System.out.println($12);
    $15 = 12;
    $14 = root.Search($15);
    System.out.println($14);
    $17 = 16;
    $16 = root.Search($17);
    System.out.println($16);
    $19 = 50;
    $18 = root.Search($19);
    System.out.println($18);
    $21 = 12;
    $20 = root.Search($21);
    System.out.println($20);
    $22 = 12;
    ntb = root.Delete($22);
    ntb = root.Print();
    $24 = 12;
    $23 = root.Search($24);
    System.out.println($23);
    $25 = 0;
    return $25;
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
    boolean $0;
    key = v_key;
    has_left = false;
    has_right = false;
    $0 = true;
    return $0;
  }
  public boolean SetRight(Tree rn) {
    boolean $0;
    right = rn;
    $0 = true;
    return $0;
  }
  public boolean SetLeft(Tree ln) {
    boolean $0;
    left = ln;
    $0 = true;
    return $0;
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
    boolean $0;
    key = v_key;
    $0 = true;
    return $0;
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
    boolean $0;
    has_left = val;
    $0 = true;
    return $0;
  }
  public boolean SetHas_Right(boolean val) {
    boolean $0;
    has_right = val;
    $0 = true;
    return $0;
  }
  public boolean Compare(int num1, int num2) {
    boolean ntb;
    int nti;
    int $0;
    boolean $1;
    boolean $2;
    boolean $3;
    ntb = false;
    $0 = 1;
    nti = num2 + $0;
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
    boolean ntb;
    Tree current_node;
    boolean cont;
    int key_aux;
    boolean $0;
    boolean $1;
    boolean $2;
    boolean $3;
    boolean $4;
    boolean $5;
    new_node = new Tree();
    ntb = new_node.Init(v_key);
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
          $2 = true;
          ntb = current_node.SetHas_Left($2);
          ntb = current_node.SetLeft(new_node);
        }
      } else {
        $3 = current_node.GetHas_Right();
        if ($3) {
          current_node = current_node.GetRight();
        } else {
          cont = false;
          $4 = true;
          ntb = current_node.SetHas_Right($4);
          ntb = current_node.SetRight(new_node);
        }
      }
    }
    $5 = true;
    return $5;
  }
  public boolean Delete(int v_key) {
    Tree current_node;
    Tree parent_node;
    boolean cont;
    boolean found;
    boolean ntb;
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
      $0 = v_key < key_aux;
      if ($0) {
        $1 = current_node.GetHas_Left();
        if ($1) {
          parent_node = current_node;
          current_node = current_node.GetLeft();
        } else {
          cont = false;
        }
      } else {
        $2 = key_aux < v_key;
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
              ntb = true;
            } else {
              ntb = this.Remove(parent_node, current_node);
            }
          } else {
            ntb = this.Remove(parent_node, current_node);
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
    boolean ntb;
    int auxkey1;
    int auxkey2;
    boolean $0;
    boolean $1;
    Tree $2;
    boolean $3;
    Tree $4;
    boolean $5;
    Tree $6;
    boolean $7;
    boolean $8;
    $0 = c_node.GetHas_Left();
    if ($0) {
      ntb = this.RemoveLeft(p_node, c_node);
    } else {
      $1 = c_node.GetHas_Right();
      if ($1) {
        ntb = this.RemoveRight(p_node, c_node);
      } else {
        auxkey1 = c_node.GetKey();
        $2 = p_node.GetLeft();
        auxkey2 = $2.GetKey();
        $3 = this.Compare(auxkey1, auxkey2);
        if ($3) {
          $4 = my_null;
          ntb = p_node.SetLeft($4);
          $5 = false;
          ntb = p_node.SetHas_Left($5);
        } else {
          $6 = my_null;
          ntb = p_node.SetRight($6);
          $7 = false;
          ntb = p_node.SetHas_Right($7);
        }
      }
    }
    $8 = true;
    return $8;
  }
  public boolean RemoveRight(Tree p_node, Tree c_node) {
    boolean ntb;
    boolean $0;
    int $1;
    Tree $2;
    Tree $3;
    boolean $4;
    boolean $5;
    $0 = c_node.GetHas_Right();
    while ($0) {
      $2 = c_node.GetRight();
      $1 = $2.GetKey();
      ntb = c_node.SetKey($1);
      p_node = c_node;
      c_node = c_node.GetRight();
      $0 = c_node.GetHas_Right();
    }
    $3 = my_null;
    ntb = p_node.SetRight($3);
    $4 = false;
    ntb = p_node.SetHas_Right($4);
    $5 = true;
    return $5;
  }
  public boolean RemoveLeft(Tree p_node, Tree c_node) {
    boolean ntb;
    boolean $0;
    int $1;
    Tree $2;
    Tree $3;
    boolean $4;
    boolean $5;
    $0 = c_node.GetHas_Left();
    while ($0) {
      $2 = c_node.GetLeft();
      $1 = $2.GetKey();
      ntb = c_node.SetKey($1);
      p_node = c_node;
      c_node = c_node.GetLeft();
      $0 = c_node.GetHas_Left();
    }
    $3 = my_null;
    ntb = p_node.SetLeft($3);
    $4 = false;
    ntb = p_node.SetHas_Left($4);
    $5 = true;
    return $5;
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
    boolean ntb;
    Tree current_node;
    boolean $0;
    current_node = this;
    ntb = this.RecPrint(current_node);
    $0 = true;
    return $0;
  }
  public boolean RecPrint(Tree node) {
    boolean ntb;
    boolean $0;
    Tree $1;
    int $2;
    boolean $3;
    Tree $4;
    boolean $5;
    $0 = node.GetHas_Left();
    if ($0) {
      $1 = node.GetLeft();
      ntb = this.RecPrint($1);
    } else {
      ntb = true;
    }
    $2 = node.GetKey();
    System.out.println($2);
    $3 = node.GetHas_Right();
    if ($3) {
      $4 = node.GetRight();
      ntb = this.RecPrint($4);
    } else {
      ntb = true;
    }
    $5 = true;
    return $5;
  }
  public int accept(Visitor v) {
    int nti;
    int $0;
    Tree $1;
    int $2;
    $0 = 333;
    System.out.println($0);
    $1 = this;
    nti = v.visit($1);
    $2 = 0;
    return $2;
  }
}
class Visitor {
  Tree l;
  Tree r;
  public int visit(Tree n) {
    int nti;
    boolean $0;
    Tree $1;
    Visitor $2;
    boolean $3;
    Tree $4;
    Visitor $5;
    int $6;
    $0 = n.GetHas_Right();
    if ($0) {
      r = n.GetRight();
      $1 = r;
      $2 = this;
      nti = $1.accept($2);
    } else {
      nti = 0;
    }
    $3 = n.GetHas_Left();
    if ($3) {
      l = n.GetLeft();
      $4 = l;
      $5 = this;
      nti = $4.accept($5);
    } else {
      nti = 0;
    }
    $6 = 0;
    return $6;
  }
}
class MyVisitor extends Visitor {
  public int visit(Tree n) {
    int nti;
    boolean $0;
    Tree $1;
    MyVisitor $2;
    int $3;
    boolean $4;
    Tree $5;
    MyVisitor $6;
    int $7;
    $0 = n.GetHas_Right();
    if ($0) {
      r = n.GetRight();
      $1 = r;
      $2 = this;
      nti = $1.accept($2);
    } else {
      nti = 0;
    }
    $3 = n.GetKey();
    System.out.println($3);
    $4 = n.GetHas_Left();
    if ($4) {
      l = n.GetLeft();
      $5 = l;
      $6 = this;
      nti = $5.accept($6);
    } else {
      nti = 0;
    }
    $7 = 0;
    return $7;
  }
}
