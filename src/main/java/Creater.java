import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Creater {

    private static final String mysqlUrl = "jdbc:mysql://ip:端口号/数据库";
    private static final String account = "账号";
    private static final String password = "密码";
    private static final String author = "作者";
    private static final String structName = "com.test.test";
    // 是否开启swagger2
    private static final boolean swagger = true;

    public static void main(String[] args) throws SQLException {
        // 查询数据库表名
        List<String> list = getAllTableName();
        // 调用代码生成方法
        for(String s : list){
            mybatisCreate(s);
        }

    }

    public static void mybatisCreate(String tableName){
        // 1、创建代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 2、全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(author);
        gc.setOpen(false); //生成后是否打开资源管理器
        gc.setFileOverride(false); //重新生成时文件是否覆盖
        gc.setServiceName("%sService"); //去掉Service接口的首字母I
        gc.setIdType(IdType.AUTO); //主键策略
        gc.setDateType(DateType.ONLY_DATE);//定义生成的实体类中日期类型
        gc.setSwagger2(swagger);//开启Swagger2模式

        mpg.setGlobalConfig(gc);

        // 3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(mysqlUrl);
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername(account);
        dsc.setPassword(password);
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        // 4、包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(null); //模块名
        pc.setParent(structName);
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 5、策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude(tableName);//对那一张表生成代码
        strategy.setNaming(NamingStrategy.underline_to_camel);//数据库表映射到实体的命名策略
        strategy.setTablePrefix(pc.getModuleName() + "_"); //生成实体时去掉表前缀
        strategy.entityTableFieldAnnotationEnable(true);//实体属性上添加表字段映射
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);//数据库表字段映射到实体的命名策略
        strategy.setEntityLombokModel(true); // lombok 模型 @Accessors(chain = true) setter链式操作

        strategy.setRestControllerStyle(true); //restful api风格控制器
        strategy.setControllerMappingHyphenStyle(true); //url中驼峰转连字符

        mpg.setStrategy(strategy);

        // 6、执行
        mpg.execute();
    }

    public static List<String> getAllTableName() throws SQLException {
        //注册驱动程序
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        //获得连接
        Connection con = DriverManager.getConnection(mysqlUrl, account, password);
        System.out.println("Connection established......");
        //创建一个Statement对象
        Statement stmt = con.createStatement();
        //检索数据
        ResultSet rs = stmt.executeQuery("Show tables");
        System.out.println("Tables in the current database: ");
        List<String> strings = new ArrayList<>();
        while(rs.next()) {
            strings.add(rs.getString(1));
            System.out.print(rs.getString(1));
            System.out.println();
        }
        return strings;
    }

}
