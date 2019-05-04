package course;

/**
* @Description: This is the interface that supports read and write operation
* @Author: Zhizhou Qiu
* @Date: 2019/4/28
**/
public interface IO<Type> {
    Type readFromFile(String path);

    void writeToFile(String path);
}
