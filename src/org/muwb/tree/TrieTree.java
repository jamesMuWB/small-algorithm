package org.muwb.tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Created by muwb on 2017/11/17.
 * 字典树，又称前缀树（英文名：Trie Tree），为 Edward Fredkin 发明
 */
public class TrieTree {
    private Node root;//根节点
    private char basicChar = '!';

    public TrieTree(){
        root = new Node();
    }

    /**
     * 插入字串，用循环代替迭代实现
     */
    public void insert(String words){
        insert(this.root, words);
    }

    private void insert(Node root,String word){
        word = word.toLowerCase();//转化为小写
        char[] chars = word.toCharArray();
        for(int i = 0, length = chars.length; i<length; i++){
            int index = (int)chars[i] - basicChar;//用相对于a字母的值作为下标索引
            if(root.childs[index] != null){
                root.childs[index].prefixNum++;//已经存在了,该子节点prefixNum++
            }else{//如果不存在
                root.childs[index] = new Node();
                root.childs[index].prefixNum++;
            }

            //如果到了字串结尾，则做标记
            if(i == length - 1){
                root.childs[index].isLeaf = true;
                root.childs[index].repeatedNum++;
            }

            root = root.childs[index];//root指向子节点，继续处理
        }
    }

    /**
     * 遍历Trie树，查找所有的words以及出现次数
     */
    public HashMap<String,Integer> getAllWords(){
        return preTraversal(this.root, "");
    }

    /**
     * 前序遍历
     */
    private  HashMap<String,Integer> preTraversal(Node root,String prefixs){
        HashMap<String, Integer> map=new HashMap<String, Integer>();

        if(root!=null){

            if(root.isLeaf==true){
                //当前即为一个单词
                map.put(prefixs, root.repeatedNum);
            }

            for(int i=0,length=root.childs.length; i<length;i++){
                if(root.childs[i]!=null){
                    char ch=(char) (i+basicChar);
                    //递归调用
                    String tempStr=prefixs+ch;
                    map.putAll(preTraversal(root.childs[i], tempStr));
                }
            }
        }

        return map;
    }

    /**
     * 判断某字串是否在字典树中
     */
    public boolean isExist(String word){
        return search(this.root, word);
    }

    private boolean search(Node root,String word){
        char[] chs=word.toLowerCase().toCharArray();
        for(int i=0,length=chs.length; i<length;i++){
            int index=chs[i]-basicChar;
            if(root.childs[index]==null){
                ///如果不存在，则查找失败
                return false;
            }
            root=root.childs[index];
        }

        return true;
    }

    /**
     * 得到以某字串为前缀的字串集，包括字串本身！ 类似单词输入法的联想功能
     */
    public HashMap<String, Integer> getWordsForPrefix(String prefix){
        return getWordsForPrefix(this.root, prefix);
    }


    private HashMap<String, Integer> getWordsForPrefix(Node root,String prefix){
        HashMap<String, Integer> map=new HashMap<String, Integer>();
        char[] chrs=prefix.toLowerCase().toCharArray();
        ////
        for(int i=0, length=chrs.length; i<length; i++){

            int index=chrs[i]-basicChar;
            if(root.childs[index]==null){
                return null;
            }

            root=root.childs[index];

        }
        ///结果包括该前缀本身
        ///此处利用之前的前序搜索方法进行搜索
        return preTraversal(root, prefix);
    }


    /**
     * 内部节点类
     */
    private class Node{
        private int repeatedNum;//该字串的重复次数,该属性统计重复次数的时候有用
        private int prefixNum;//以该字串为前缀的字串数,应该包括该字串本身
        private Node childs[];
        private boolean isLeaf;
        public Node(){
            repeatedNum = 0;
            prefixNum=0;
            isLeaf=false;
            childs=new Node[128-30+1];
        }
    }

    public static void main(String[] args){
        try {
            //读取测试文件
            String file = System.getProperty("user.dir") + "/src/org/muwb/tree/test.txt";
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            TrieTree tt = new TrieTree();
            while ((s = br.readLine()) != null) {
                String[] arr = s.split(" ");
                for(String a : arr) {
                    a = a.replace(",", "").replace(".", "");
                    tt.insert(a);
                }
            }
            br.close();


            HashMap<String,Integer> map = tt.getAllWords();

            for(String key:map.keySet())
                System.out.println(key + " 出现: "+ map.get(key) + "次");

            map=tt.getWordsForPrefix("Daisy");

            System.out.println("\n\n包含Daisy（包括本身）前缀的单词及出现次数：");
            for(String key:map.keySet()){
                System.out.println(key+" 出现: "+ map.get(key)+"次");
            }

            if(tt.isExist("muwb")==false){
                System.out.println("\n\n字典树中不存在：muwb ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}

