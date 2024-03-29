##长按识别二维码
### **界面效果**
![image](https://img-blog.csdnimg.cn/direct/3a00671b179143d6b70a50a13cd76349.gif)

### **特点**

1. **长按(其他触发方式)识别指定区域的二维码**
2. **自定义识别区域**
3. **结果点的颜色,大小等可以灵活定义**



|版本号|制定人|制日期|修订日期|
|:
|7.6.0 | 刘朝辉 |2023年10月9日 |-|
|1|2|3|34|
#### 请求URL:

- /check/zhzfcommandtask/statistics

*****
code print('地点水电费水电费')

#### 请求方式：
- POST

#### 请求头：
|参数名|是否必须|类型|说明|版本号|
|:----|:----|:----|----|
|Content-Type |是|string |请求类型：application/json|6.5.1|
|X-AUTH-TOKEN|是|String|当前用户token|6.5.1|

#### 返回示例:
```
{
    "code": 0,
    "body": {
        "incomplete": 1,//指挥任务未完成数量
        "completedBFB": "0%",//指挥任务完成数量百分比
        "sum": 1,//指挥任务总数
        "unapprovedBFB": "0%",//指挥任务待审批百分比
        "completed": 0,//指挥任务完成数量
        "unapproved": 0//指挥任务待审批
    },
    "message": "success"
}
```

#### 备注:
以下是错误码表：
 0 成功
-9999 系统错误
