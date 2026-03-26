class BinaryTree {
    public static void main(String[] a) {
        int TEMP_1;
        BT TEMP_0;
        TEMP_0 = new BT();
        TEMP_1 = TEMP_0.Start();
        System.out.println(TEMP_1);
    }
}

class BT {
    public int Start() {
        Tree root;
        boolean ntb;
        int nti;
        Tree TEMP_2;
        int TEMP_10;
        int TEMP_9;
        int TEMP_8;
        int TEMP_7;
        int TEMP_6;
        int TEMP_5;
        int TEMP_4;
        int TEMP_3;
        TEMP_2 = new Tree();
        root = TEMP_2;
        ntb = root.Init(16);
        ntb = root.Print();
        TEMP_3 = 100000000;
        System.out.println(TEMP_3);
        ntb = root.Insert(8);
        ntb = root.Print();
        ntb = root.Insert(24);
        ntb = root.Insert(4);
        ntb = root.Insert(12);
        ntb = root.Insert(20);
        ntb = root.Insert(28);
        ntb = root.Insert(14);
        ntb = root.Print();
        TEMP_4 = root.Search(24);
        System.out.println(TEMP_4);
        TEMP_5 = root.Search(12);
        System.out.println(TEMP_5);
        TEMP_6 = root.Search(16);
        System.out.println(TEMP_6);
        TEMP_7 = root.Search(50);
        System.out.println(TEMP_7);
        TEMP_8 = root.Search(12);
        System.out.println(TEMP_8);
        ntb = root.Delete(12);
        ntb = root.Print();
        TEMP_9 = root.Search(12);
        System.out.println(TEMP_9);
        TEMP_10 = 0;
        return TEMP_10;
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
        boolean TEMP_11;
        key = v_key;
        has_left = false;
        has_right = false;
        TEMP_11 = true;
        return TEMP_11;
    }

    public boolean SetRight(Tree rn) {
        boolean TEMP_12;
        right = rn;
        TEMP_12 = true;
        return TEMP_12;
    }

    public boolean SetLeft(Tree ln) {
        boolean TEMP_13;
        left = ln;
        TEMP_13 = true;
        return TEMP_13;
    }

    public Tree GetRight() {
        Tree TEMP_14;
        TEMP_14 = right;
        return TEMP_14;
    }

    public Tree GetLeft() {
        Tree TEMP_15;
        TEMP_15 = left;
        return TEMP_15;
    }

    public int GetKey() {
        int TEMP_16;
        TEMP_16 = key;
        return TEMP_16;
    }

    public boolean SetKey(int v_key) {
        boolean TEMP_17;
        key = v_key;
        TEMP_17 = true;
        return TEMP_17;
    }

    public boolean GetHas_Right() {
        boolean TEMP_18;
        TEMP_18 = has_right;
        return TEMP_18;
    }

    public boolean GetHas_Left() {
        boolean TEMP_19;
        TEMP_19 = has_left;
        return TEMP_19;
    }

    public boolean SetHas_Left(boolean val) {
        boolean TEMP_20;
        has_left = val;
        TEMP_20 = true;
        return TEMP_20;
    }

    public boolean SetHas_Right(boolean val) {
        boolean TEMP_21;
        has_right = val;
        TEMP_21 = true;
        return TEMP_21;
    }

    public boolean Compare(int num1, int num2) {
        boolean ntb;
        int nti;
        boolean TEMP_23;
        boolean TEMP_24;
        boolean TEMP_25;
        boolean TEMP_26;
        boolean TEMP_22;
        ntb = false;
        nti = num2 + 1;
        TEMP_22 = num1 < num2;
        if (TEMP_22) {
            ntb = false;
        } else {
            TEMP_23 = num1 < nti;
            TEMP_24 = !TEMP_23;
            TEMP_25 = TEMP_24;
            if (TEMP_25) {
                ntb = false;
            } else {
                ntb = true;
            }
        }
        TEMP_26 = ntb;
        return TEMP_26;
    }

    public boolean Insert(int v_key) {
        Tree new_node;
        boolean ntb;
        boolean cont;
        int key_aux;
        Tree current_node;
        boolean TEMP_30;
        boolean TEMP_31;
        boolean TEMP_32;
        Tree TEMP_27;
        boolean TEMP_28;
        boolean TEMP_29;
        TEMP_27 = new Tree();
        new_node = TEMP_27;
        ntb = new_node.Init(v_key);
        current_node = this;
        cont = true;
        TEMP_28 = cont;
        while (TEMP_28) {
            {
                key_aux = current_node.GetKey();

                TEMP_29 = v_key < key_aux;
                if (TEMP_29) {
                    {
                        TEMP_30 = current_node.GetHas_Left();
                        if (TEMP_30) {
                            current_node = current_node.GetLeft();
                        } else {
                            {
                                cont = false;

                                ntb = current_node.SetHas_Left(true);

                                ntb = current_node.SetLeft(new_node);

                            }
                        }
                    }
                } else {
                    {
                        TEMP_31 = current_node.GetHas_Right();
                        if (TEMP_31) {
                            current_node = current_node.GetRight();
                        } else {
                            {
                                cont = false;

                                ntb = current_node.SetHas_Right(true);

                                ntb = current_node.SetRight(new_node);

                            }
                        }
                    }
                }
            }
            TEMP_28 = cont;
        }
        TEMP_32 = true;
        return TEMP_32;
    }

    public boolean Delete(int v_key) {
        Tree current_node;
        Tree parent_node;
        boolean cont;
        boolean found;
        boolean is_root;
        int key_aux;
        boolean ntb;
        boolean TEMP_34;
        boolean TEMP_45;
        boolean TEMP_35;
        boolean TEMP_36;
        boolean TEMP_37;
        boolean TEMP_41;
        boolean TEMP_42;
        boolean TEMP_43;
        boolean TEMP_33;
        boolean TEMP_44;
        boolean TEMP_40;
        boolean TEMP_38;
        boolean TEMP_39;
        current_node = this;
        parent_node = this;
        cont = true;
        found = false;
        is_root = true;
        TEMP_33 = cont;
        while (TEMP_33) {
            {
                key_aux = current_node.GetKey();

                TEMP_34 = v_key < key_aux;
                if (TEMP_34) {
                    TEMP_35 = current_node.GetHas_Left();
                    if (TEMP_35) {
                        {
                            parent_node = current_node;

                            current_node = current_node.GetLeft();

                        }
                    } else {
                        cont = false;
                    }
                } else {
                    TEMP_36 = key_aux < v_key;
                    if (TEMP_36) {
                        TEMP_37 = current_node.GetHas_Right();
                        if (TEMP_37) {
                            {
                                parent_node = current_node;

                                current_node = current_node.GetRight();

                            }
                        } else {
                            cont = false;
                        }
                    } else {
                        {
                            TEMP_38 = is_root;
                            if (TEMP_38) {
                                TEMP_39 = current_node.GetHas_Right();
                                TEMP_40 = !TEMP_39;
                                if (TEMP_40) {
                                    TEMP_41 = current_node.GetHas_Left();
                                    TEMP_42 = !TEMP_41;
                                    TEMP_43 = TEMP_42;
                                } else {
                                    TEMP_43 = false;
                                }
                                TEMP_44 = TEMP_43;
                                if (TEMP_44) {
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
            TEMP_33 = cont;
        }
        TEMP_45 = found;
        return TEMP_45;
    }

    public boolean Remove(Tree p_node, Tree c_node) {
        boolean ntb;
        int auxkey1;
        int auxkey2;
        boolean TEMP_46;
        boolean TEMP_47;
        boolean TEMP_48;
        boolean TEMP_49;
        TEMP_46 = c_node.GetHas_Left();
        if (TEMP_46) {
            ntb = this.RemoveLeft(p_node, c_node);
        } else {
            TEMP_47 = c_node.GetHas_Right();
            if (TEMP_47) {
                ntb = this.RemoveRight(p_node, c_node);
            } else {
                {
                    auxkey1 = c_node.GetKey();

                    auxkey2 = p_node.GetLeft().GetKey();

                    TEMP_48 = this.Compare(auxkey1, auxkey2);
                    if (TEMP_48) {
                        {
                            ntb = p_node.SetLeft(my_null);

                            ntb = p_node.SetHas_Left(false);

                        }
                    } else {
                        {
                            ntb = p_node.SetRight(my_null);

                            ntb = p_node.SetHas_Right(false);

                        }
                    }
                }
            }
        }
        TEMP_49 = true;
        return TEMP_49;
    }

    public boolean RemoveRight(Tree p_node, Tree c_node) {
        boolean ntb;
        boolean TEMP_50;
        boolean TEMP_51;
        TEMP_50 = c_node.GetHas_Right();
        while (TEMP_50) {
            {
                ntb = c_node.SetKey(c_node.GetRight().GetKey());

                p_node = c_node;

                c_node = c_node.GetRight();

            }
            TEMP_50 = c_node.GetHas_Right();
        }
        ntb = p_node.SetRight(my_null);
        ntb = p_node.SetHas_Right(false);
        TEMP_51 = true;
        return TEMP_51;
    }

    public boolean RemoveLeft(Tree p_node, Tree c_node) {
        boolean ntb;
        boolean TEMP_52;
        boolean TEMP_53;
        TEMP_52 = c_node.GetHas_Left();
        while (TEMP_52) {
            {
                ntb = c_node.SetKey(c_node.GetLeft().GetKey());

                p_node = c_node;

                c_node = c_node.GetLeft();

            }
            TEMP_52 = c_node.GetHas_Left();
        }
        ntb = p_node.SetLeft(my_null);
        ntb = p_node.SetHas_Left(false);
        TEMP_53 = true;
        return TEMP_53;
    }

    public int Search(int v_key) {
        boolean cont;
        int ifound;
        Tree current_node;
        int key_aux;
        boolean TEMP_56;
        boolean TEMP_57;
        boolean TEMP_58;
        int TEMP_59;
        boolean TEMP_54;
        boolean TEMP_55;
        current_node = this;
        cont = true;
        ifound = 0;
        TEMP_54 = cont;
        while (TEMP_54) {
            {
                key_aux = current_node.GetKey();

                TEMP_55 = v_key < key_aux;
                if (TEMP_55) {
                    TEMP_56 = current_node.GetHas_Left();
                    if (TEMP_56) {
                        current_node = current_node.GetLeft();
                    } else {
                        cont = false;
                    }
                } else {
                    TEMP_57 = key_aux < v_key;
                    if (TEMP_57) {
                        TEMP_58 = current_node.GetHas_Right();
                        if (TEMP_58) {
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
            TEMP_54 = cont;
        }
        TEMP_59 = ifound;
        return TEMP_59;
    }

    public boolean Print() {
        Tree current_node;
        boolean ntb;
        boolean TEMP_60;
        current_node = this;
        ntb = this.RecPrint(current_node);
        TEMP_60 = true;
        return TEMP_60;
    }

    public boolean RecPrint(Tree node) {
        boolean ntb;
        boolean TEMP_63;
        boolean TEMP_64;
        boolean TEMP_61;
        int TEMP_62;
        TEMP_61 = node.GetHas_Left();
        if (TEMP_61) {
            {
                ntb = this.RecPrint(node.GetLeft());

            }
        } else {
            ntb = true;
        }
        TEMP_62 = node.GetKey();
        System.out.println(TEMP_62);
        TEMP_63 = node.GetHas_Right();
        if (TEMP_63) {
            {
                ntb = this.RecPrint(node.GetRight());

            }
        } else {
            ntb = true;
        }
        TEMP_64 = true;
        return TEMP_64;
    }

}
