class TreeVisitor {
    public static void main(String[] a) {
        int _t0;
        _t0 = new TV().Start();
        System.out.println(_t0);
    }
}

class TV {
    public int Start() {
        Tree root;
        boolean ntb;
        int nti;
        MyVisitor v;
        int _t1;
        int _t2;
        int _t3;
        int _t4;
        int _t5;
        int _t6;
        int _t7;
        int _t8;
        int _t9;
        int _t10;
        int _t11;
        int _t12;
        int _t13;
        int _t14;
        int _t15;
        int _t16;
        int _t17;
        int _t18;
        int _t19;
        int _t20;
        int _t21;
        int _t22;
        int _t23;
        int _t24;
        int _t25;
        int _t26;
        root = new Tree();
        _t1 = 16;
        ntb = root.Init(_t1);
        ntb = root.Print();
        _t2 = 100000000;
        System.out.println(_t2);
        _t3 = 8;
        ntb = root.Insert(_t3);
        _t4 = 24;
        ntb = root.Insert(_t4);
        _t5 = 4;
        ntb = root.Insert(_t5);
        _t6 = 12;
        ntb = root.Insert(_t6);
        _t7 = 20;
        ntb = root.Insert(_t7);
        _t8 = 28;
        ntb = root.Insert(_t8);
        _t9 = 14;
        ntb = root.Insert(_t9);
        ntb = root.Print();
        _t10 = 100000000;
        System.out.println(_t10);
        v = new MyVisitor();
        _t11 = 50000000;
        System.out.println(_t11);
        nti = root.accept(v);
        _t12 = 100000000;
        System.out.println(_t12);
        _t13 = 24;
        _t14 = root.Search(_t13);
        System.out.println(_t14);
        _t15 = 12;
        _t16 = root.Search(_t15);
        System.out.println(_t16);
        _t17 = 16;
        _t18 = root.Search(_t17);
        System.out.println(_t18);
        _t19 = 50;
        _t20 = root.Search(_t19);
        System.out.println(_t20);
        _t21 = 12;
        _t22 = root.Search(_t21);
        System.out.println(_t22);
        _t23 = 12;
        ntb = root.Delete(_t23);
        ntb = root.Print();
        _t24 = 12;
        _t25 = root.Search(_t24);
        System.out.println(_t25);
        _t26 = 0;
        return _t26;
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
        boolean _t27;
        key = v_key;
        has_left = false;
        has_right = false;
        _t27 = true;
        return _t27;
    }

    public boolean SetRight(Tree rn) {
        boolean _t28;
        right = rn;
        _t28 = true;
        return _t28;
    }

    public boolean SetLeft(Tree ln) {
        boolean _t29;
        left = ln;
        _t29 = true;
        return _t29;
    }

    public Tree GetRight() {
        return right;
    }

    public Tree GetLeft() {
        return left;
    }

    public int GetKey() {
        return key;
    }

    public boolean SetKey(int v_key) {
        boolean _t30;
        key = v_key;
        _t30 = true;
        return _t30;
    }

    public boolean GetHas_Right() {
        return has_right;
    }

    public boolean GetHas_Left() {
        return has_left;
    }

    public boolean SetHas_Left(boolean val) {
        boolean _t31;
        has_left = val;
        _t31 = true;
        return _t31;
    }

    public boolean SetHas_Right(boolean val) {
        boolean _t32;
        has_right = val;
        _t32 = true;
        return _t32;
    }

    public boolean Compare(int num1, int num2) {
        boolean ntb;
        int nti;
        int _t33;
        boolean _t34;
        boolean _t35;
        boolean _t36;
        ntb = false;
        _t33 = 1;
        nti = num2 + _t33;
        _t34 = num1 < num2;
        if (_t34) {
            ntb = false;
        } else {
            _t35 = num1 < nti;
            _t36 = !_t35;
            if (_t36) {
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
        boolean _t37;
        boolean _t38;
        boolean _t39;
        boolean _t40;
        boolean _t41;
        boolean _t42;
        new_node = new Tree();
        ntb = new_node.Init(v_key);
        current_node = this;
        cont = true;
        while (cont) {
            {
                key_aux = current_node.GetKey();
                _t37 = v_key < key_aux;
                if (_t37) {
                    {
                        _t38 = current_node.GetHas_Left();
                        if (_t38) {
                            current_node = current_node.GetLeft();
                        } else {
                            {
                                cont = false;
                                _t39 = true;
                                ntb = current_node.SetHas_Left(_t39);
                                ntb = current_node.SetLeft(new_node);
                            }
                        }
                    }
                } else {
                    {
                        _t40 = current_node.GetHas_Right();
                        if (_t40) {
                            current_node = current_node.GetRight();
                        } else {
                            {
                                cont = false;
                                _t41 = true;
                                ntb = current_node.SetHas_Right(_t41);
                                ntb = current_node.SetRight(new_node);
                            }
                        }
                    }
                }
            }
            cont = cont;
        }
        _t42 = true;
        return _t42;
    }

    public boolean Delete(int v_key) {
        Tree current_node;
        Tree parent_node;
        boolean cont;
        boolean found;
        boolean ntb;
        boolean is_root;
        int key_aux;
        boolean _t43;
        boolean _t44;
        boolean _t45;
        boolean _t46;
        boolean _t47;
        boolean _t48;
        boolean _t49;
        boolean _t50;
        boolean _t51;
        current_node = this;
        parent_node = this;
        cont = true;
        found = false;
        is_root = true;
        while (cont) {
            {
                key_aux = current_node.GetKey();
                _t43 = v_key < key_aux;
                if (_t43) {
                    _t44 = current_node.GetHas_Left();
                    if (_t44) {
                        {
                            parent_node = current_node;
                            current_node = current_node.GetLeft();
                        }
                    } else {
                        cont = false;
                    }
                } else {
                    _t45 = key_aux < v_key;
                    if (_t45) {
                        _t46 = current_node.GetHas_Right();
                        if (_t46) {
                            {
                                parent_node = current_node;
                                current_node = current_node.GetRight();
                            }
                        } else {
                            cont = false;
                        }
                    } else {
                        {
                            if (is_root) {
                                _t47 = current_node.GetHas_Right();
                                _t48 = !_t47;
                                _t49 = current_node.GetHas_Left();
                                _t50 = !_t49;
                                if (_t48) {
                                    if (_t50) {
                                        _t51 = true;
                                    } else {
                                        _t51 = false;
                                    }
                                } else {
                                    _t51 = false;
                                }
                                if (_t51) {
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
                }
                is_root = false;
            }
            cont = cont;
        }
        return found;
    }

    public boolean Remove(Tree p_node, Tree c_node) {
        boolean ntb;
        int auxkey1;
        int auxkey2;
        boolean _t52;
        boolean _t53;
        Tree _t54;
        boolean _t55;
        boolean _t56;
        boolean _t57;
        boolean _t58;
        _t52 = c_node.GetHas_Left();
        if (_t52) {
            ntb = this.RemoveLeft(p_node, c_node);
        } else {
            _t53 = c_node.GetHas_Right();
            if (_t53) {
                ntb = this.RemoveRight(p_node, c_node);
            } else {
                {
                    auxkey1 = c_node.GetKey();
                    _t54 = p_node.GetLeft();
                    auxkey2 = _t54.GetKey();
                    _t55 = this.Compare(auxkey1, auxkey2);
                    if (_t55) {
                        {
                            ntb = p_node.SetLeft(my_null);
                            _t56 = false;
                            ntb = p_node.SetHas_Left(_t56);
                        }
                    } else {
                        {
                            ntb = p_node.SetRight(my_null);
                            _t57 = false;
                            ntb = p_node.SetHas_Right(_t57);
                        }
                    }
                }
            }
        }
        _t58 = true;
        return _t58;
    }

    public boolean RemoveRight(Tree p_node, Tree c_node) {
        boolean ntb;
        boolean _t59;
        Tree _t60;
        int _t61;
        boolean _t62;
        boolean _t63;
        _t59 = c_node.GetHas_Right();
        while (_t59) {
            {
                _t60 = c_node.GetRight();
                _t61 = _t60.GetKey();
                ntb = c_node.SetKey(_t61);
                p_node = c_node;
                c_node = c_node.GetRight();
            }
            _t59 = c_node.GetHas_Right();
        }
        ntb = p_node.SetRight(my_null);
        _t62 = false;
        ntb = p_node.SetHas_Right(_t62);
        _t63 = true;
        return _t63;
    }

    public boolean RemoveLeft(Tree p_node, Tree c_node) {
        boolean ntb;
        boolean _t64;
        Tree _t65;
        int _t66;
        boolean _t67;
        boolean _t68;
        _t64 = c_node.GetHas_Left();
        while (_t64) {
            {
                _t65 = c_node.GetLeft();
                _t66 = _t65.GetKey();
                ntb = c_node.SetKey(_t66);
                p_node = c_node;
                c_node = c_node.GetLeft();
            }
            _t64 = c_node.GetHas_Left();
        }
        ntb = p_node.SetLeft(my_null);
        _t67 = false;
        ntb = p_node.SetHas_Left(_t67);
        _t68 = true;
        return _t68;
    }

    public int Search(int v_key) {
        Tree current_node;
        int ifound;
        boolean cont;
        int key_aux;
        boolean _t69;
        boolean _t70;
        boolean _t71;
        boolean _t72;
        current_node = this;
        cont = true;
        ifound = 0;
        while (cont) {
            {
                key_aux = current_node.GetKey();
                _t69 = v_key < key_aux;
                if (_t69) {
                    _t70 = current_node.GetHas_Left();
                    if (_t70) {
                        current_node = current_node.GetLeft();
                    } else {
                        cont = false;
                    }
                } else {
                    _t71 = key_aux < v_key;
                    if (_t71) {
                        _t72 = current_node.GetHas_Right();
                        if (_t72) {
                            current_node = current_node.GetRight();
                        } else {
                            cont = false;
                        }
                    } else {
                        {
                            ifound = 1;
                            cont = false;
                        }
                    }
                }
            }
            cont = cont;
        }
        return ifound;
    }

    public boolean Print() {
        boolean ntb;
        Tree current_node;
        boolean _t73;
        current_node = this;
        ntb = this.RecPrint(current_node);
        _t73 = true;
        return _t73;
    }

    public boolean RecPrint(Tree node) {
        boolean ntb;
        boolean _t74;
        Tree _t75;
        int _t76;
        boolean _t77;
        Tree _t78;
        boolean _t79;
        _t74 = node.GetHas_Left();
        if (_t74) {
            {
                _t75 = node.GetLeft();
                ntb = this.RecPrint(_t75);
            }
        } else {
            ntb = true;
        }
        _t76 = node.GetKey();
        System.out.println(_t76);
        _t77 = node.GetHas_Right();
        if (_t77) {
            {
                _t78 = node.GetRight();
                ntb = this.RecPrint(_t78);
            }
        } else {
            ntb = true;
        }
        _t79 = true;
        return _t79;
    }

    public int accept(Visitor v) {
        int nti;
        int _t80;
        Tree _t81;
        int _t82;
        _t80 = 333;
        System.out.println(_t80);
        _t81 = this;
        nti = v.visit(_t81);
        _t82 = 0;
        return _t82;
    }

}

class Visitor {
    Tree l;
    Tree r;

    public int visit(Tree n) {
        int nti;
        boolean _t83;
        Visitor _t84;
        boolean _t85;
        Visitor _t86;
        int _t87;
        _t83 = n.GetHas_Right();
        if (_t83) {
            {
                r = n.GetRight();
                _t84 = this;
                nti = r.accept(_t84);
            }
        } else {
            nti = 0;
        }
        _t85 = n.GetHas_Left();
        if (_t85) {
            {
                l = n.GetLeft();
                _t86 = this;
                nti = l.accept(_t86);
            }
        } else {
            nti = 0;
        }
        _t87 = 0;
        return _t87;
    }

}

class MyVisitor extends Visitor {
    public int visit(Tree n) {
        int nti;
        boolean _t88;
        MyVisitor _t89;
        int _t90;
        boolean _t91;
        MyVisitor _t92;
        int _t93;
        _t88 = n.GetHas_Right();
        if (_t88) {
            {
                r = n.GetRight();
                _t89 = this;
                nti = r.accept(_t89);
            }
        } else {
            nti = 0;
        }
        _t90 = n.GetKey();
        System.out.println(_t90);
        _t91 = n.GetHas_Left();
        if (_t91) {
            {
                l = n.GetLeft();
                _t92 = this;
                nti = l.accept(_t92);
            }
        } else {
            nti = 0;
        }
        _t93 = 0;
        return _t93;
    }

}
