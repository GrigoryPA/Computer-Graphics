import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class RayTracing3D {
    private Vector3d camera = new Vector3d(0,60,0);
    private int dirZ = 1;
    private double fov = Math.PI/2;
    public  Vector<Sphere3D> AllSpheres;
    public  Vector<Light3D> AllLights;
    public  Vector<TriangleModel> AllTriangleModels;
    public  Sphere3D OneSphere;
    public  Light3D OneLight;
    public  TriangleModel OneTriangleModel;
   // private  Vector3d BackgroundColor = new Vector3d(0.3,0.5,0.4);
    private  int maxDepth=4;
    public int width;
    public int height;
    public Display3D Display;
    private int threadsAmount = 8;

    public RayTracing3D(JTable TableSpheres, JTable TableLights, Display3D _display){
        Display = _display;
        width =  Display.widthVirtualDisplay;
        height = Display.heightVirtualDisplay;
        AllSpheres = new Vector<Sphere3D>();
        AllLights = new Vector<Light3D>();
        AllTriangleModels = new Vector<TriangleModel>();

        for(int i=0; i<TableSpheres.getRowCount(); i++) {
            OneSphere = new Sphere3D(TableSpheres, i);
            AllSpheres.add(OneSphere);
        }

        for(int i=0; i<TableLights.getRowCount(); i++) {
            OneLight = new Light3D(TableLights, i);
            AllLights.add(OneLight);
        }

        OneTriangleModel = new TriangleModel( "src/main/resources/3d/duck.obj",
                MaterialType.REDWOOD);
        double x = -5.10949;
        double y = 4.13437;
        double z = 9.007105;
        OneTriangleModel.MoveModel(0+x, 40+y,60+z);
        AllTriangleModels.add(OneTriangleModel);
        //пол
        OneTriangleModel = new TriangleModel(new Vector3d(-105, 0, -35),
                new Vector3d(-105, 0, 100),
                new Vector3d(105,0,100),
                new Vector3d(105, 0, -35),
                MaterialType.STEEL);
        AllTriangleModels.add(OneTriangleModel);

        //потолок
        OneTriangleModel = new TriangleModel(new Vector3d(-105, 100, -35),
                new Vector3d(105,100 ,-35),
                new Vector3d(105,100,100),
                new Vector3d(-105, 100,100),
                MaterialType.STEEL);
        AllTriangleModels.add(OneTriangleModel);

        //левая ближняя стена
        OneTriangleModel = new TriangleModel(new Vector3d(0,0,-35),
                new Vector3d(0,100,-35),
                new Vector3d(-105,100,70),
                new Vector3d(-105,0,70),
                MaterialType.STEEL);
        AllTriangleModels.add(OneTriangleModel);

        //правая ближняя стена
        OneTriangleModel = new TriangleModel(new Vector3d(0,0,-35),
                new Vector3d(105, 0, 70),
                new Vector3d(105, 100, 70),
                new Vector3d(0, 100, -35),
                MaterialType.STEEL);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для большого правого зеркала (нижний брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(90,0,100),
                new Vector3d(90,4,100),
                new Vector3d(86.4,4,98.2),
                new Vector3d(86.4, 0, 98.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого правого зеркала (нижний брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(86.4,0,98.2),
                new Vector3d(86.4,4,98.2),
                new Vector3d(101.37,4,68.1),
                new Vector3d(101.37, 0, 68.1),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого правого зеркала (нижний брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(101.37,0,68.1),
                new Vector3d(101.37,4,68.1),
                new Vector3d(105,4,70),
                new Vector3d(105, 0, 70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого правого зеркала (нижний брусок верх)
        OneTriangleModel = new TriangleModel(new Vector3d(86.4,4,98.2),
                new Vector3d(90,4,100),
                new Vector3d(105,4, 70),
                new Vector3d(101.37, 4, 68.1),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для большого правого зеркала (левый брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(90,0,100),
                new Vector3d(90,4,100),
                new Vector3d(86.4,4,98.2),
                new Vector3d(86.4, 0, 98.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого правого зеркала (левый брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(86.4,4,98.2),
                new Vector3d(86.4,96,98.2),
                new Vector3d(88.15,96,94.65),
                new Vector3d(88.15, 4, 94.65),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого правого зеркала (левый брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(88.15,4,94.65),
                new Vector3d(88.15,96,94.65),
                new Vector3d(91.8,96, 96.44),
                new Vector3d(91.8, 4, 96.44),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для большого правого зеркала (правый брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(103.15,4,73.74),
                new Vector3d(103.15,96,73.74),
                new Vector3d(99.47,96,71.95),
                new Vector3d(99.47,4,71.95),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого правого зеркала (правый брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(99.47,4,71.95),
                new Vector3d(99.47,96,71.95),
                new Vector3d(101.37,96,68.1),
                new Vector3d(101.37,4,68.1),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого правого зеркала (правый брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(101.37,4,68.1),
                new Vector3d(101.37,96,68.1),
                new Vector3d(105,96, 70),
                new Vector3d(105, 4, 70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для большого правого зеркала (верхний брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(90,96,100),
                new Vector3d(90,100,100),
                new Vector3d(86.4,100, 98.2),
                new Vector3d(86.4,96, 98.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого правого зеркала (верхний брусок ниж)
        OneTriangleModel = new TriangleModel(new Vector3d(90,100,100),
                new Vector3d(86.4,100,98.2),
                new Vector3d(101.37,100,68.1),
                new Vector3d(105,100,70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого правого зеркала (верхний брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(86.4,96,98.2),
                new Vector3d(86.4,100,98.2),
                new Vector3d(101.37,100,68.1),
                new Vector3d(101.37,96,68.1),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого правого зеркала (верхний брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(101.37,96,68.1),
                new Vector3d(101.37,100,68.1),
                new Vector3d(105,96, 70),
                new Vector3d(105, 4, 70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого правого зеркала (верхний брусок верх)
        OneTriangleModel = new TriangleModel(new Vector3d(86.4,100,98.2),
                new Vector3d(90,100,100),
                new Vector3d(105,100, 70),
                new Vector3d(101.37, 100, 68.1),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //правая дальняя стена(зеркало)
        OneTriangleModel = new TriangleModel(new Vector3d(90,0,100),
                new Vector3d(90,100,100),
                new Vector3d(105,100,70),
                new Vector3d(105, 0, 70),
                MaterialType.MIRROR);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для большого левого зеркала (нижний брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-105,0,70),
                new Vector3d(-105,4,70),
                new Vector3d(-101.37,4,68.1),
                new Vector3d(-101.37, 0, 68.1),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого левого зеркала (нижний брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-101.37,0,68.1),
                new Vector3d(-101.37,4,68.1),
                new Vector3d(-86.4,4,98.2),
                new Vector3d(-86.4,0,98.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого левого зеркала (нижний брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(-86.4,0,98.2),
                new Vector3d(-86.4,4,98.2),
                new Vector3d(-90,4,100),
                new Vector3d(-90,0,100),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого левого зеркала (нижний брусок верх)
        OneTriangleModel = new TriangleModel(new Vector3d(-105,4,70),
                new Vector3d(-90,4,100),
                new Vector3d(-86.4,4, 98.2),
                new Vector3d(-101.37, 4, 68.1),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для большого левого зеркала (левый брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-105,4,70),
                new Vector3d(-105,96,70),
                new Vector3d(-101.37,96,68.1),
                new Vector3d(-101.37, 4, 68.1),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого левого зеркала (левый брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-101.37,4,68.1),
                new Vector3d(-101.37,96,68.1),
                new Vector3d(-99.47,96,71.95),
                new Vector3d(-99.47,4,71.95),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого левого зеркала (левый брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(-99.47,4,71.95),
                new Vector3d(-99.47,96,71.95),
                new Vector3d(-103.15,96, 73.74),
                new Vector3d(-103.15,4, 73.74),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для большого левого зеркала (правый брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-91.8,4,96.44),
                new Vector3d(-91.8,96,96.44),
                new Vector3d(-88.15,96,94.65),
                new Vector3d(-88.15,4,94.65),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого левого зеркала (правый брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-88.15,4,94.65),
                new Vector3d(-88.15,96,94.65),
                new Vector3d(-86.4,96,98.2),
                new Vector3d(-86.4,4,98.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого левого зеркала (правый брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(-86.4,4,98.2),
                new Vector3d(-86.4,96,98.2),
                new Vector3d(-90,96, 100),
                new Vector3d(-90, 4, 100),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для большого левого зеркала (верхний брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-105,96,70),
                new Vector3d(-105,100,70),
                new Vector3d(-101.37,100, 68.1),
                new Vector3d(-101.37,96, 68.1),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого левого зеркала (верхний брусок ниж)
        OneTriangleModel = new TriangleModel(new Vector3d(-101.37,100,68.1),
                new Vector3d(-86.4, 100, 98.2),
                new Vector3d(-90, 100, 100),
                new Vector3d(-105, 100, 70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого левого зеркала (верхний брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-101.37,96,68.1),
                new Vector3d(-101.37,100,68.1),
                new Vector3d(-86.4, 100, 98.2),
                new Vector3d(-86.4, 96, 98.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого левого зеркала (верхний брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(-86.4,96,98.2),
                new Vector3d(-86.4,100,98.2),
                new Vector3d(-90, 100, 100),
                new Vector3d(-90, 96, 100),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для большого левого зеркала (верхний брусок верх)
        OneTriangleModel = new TriangleModel(new Vector3d(-105,100,70),
                new Vector3d(-90,100,100),
                new Vector3d(-86.4,100, 98.2),
                new Vector3d(-101.37, 100, 68.1),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //левая дальняя стена(зеркало)
        OneTriangleModel = new TriangleModel(new Vector3d(-105,0,70),
                new Vector3d(-105,100,70),
                new Vector3d(-90, 100, 100),
                new Vector3d(-90, 0, 100),
                MaterialType.MIRROR);
        AllTriangleModels.add(OneTriangleModel);


        //оконная рама (верхний брусок верх)
        OneTriangleModel = new TriangleModel(new Vector3d(-83,88,97),
                new Vector3d(-83,88,100),
                new Vector3d(83, 88, 100),
                new Vector3d(83, 88, 97),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //оконная рама (верхний брусок низ)
        OneTriangleModel = new TriangleModel(new Vector3d(-80,85,97),
                new Vector3d(80,85,97),
                new Vector3d(80, 85, 100),
                new Vector3d(-80, 85, 100),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //оконная рама (верхний брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-83,85,97),
                new Vector3d(-83,88,97),
                new Vector3d(83, 88, 97),
                new Vector3d(83, 85, 97),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //оконная рама (левый брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-83,88,100),
                new Vector3d(-83,88,97),
                new Vector3d(-83, 35, 97),
                new Vector3d(-83, 35, 100),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //оконная рама (левый брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-83,85,97),
                new Vector3d(-80,85,97),
                new Vector3d(-80, 32, 97),
                new Vector3d(-83, 32, 97),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //оконная рама (левый брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(-80,85,97),
                new Vector3d(-80,85,100),
                new Vector3d(-80, 32, 100),
                new Vector3d(-80, 32, 97),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //оконная рама (правый брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(80,85,100),
                new Vector3d(80,85,97),
                new Vector3d(80, 32, 97),
                new Vector3d(80, 32, 100),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //оконная рама (правый брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(80,85,97),
                new Vector3d(83,85,97),
                new Vector3d(83, 32, 97),
                new Vector3d(80, 32, 97),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //оконная рама (правый брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(83,88,97),
                new Vector3d(83,88,100),
                new Vector3d(83, 35, 100),
                new Vector3d(83, 35, 97),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);


        //оконная рама (нижний брусок верх)
        OneTriangleModel = new TriangleModel(new Vector3d(-80,35,97),
                new Vector3d(-80,35,100),
                new Vector3d(80, 35, 100),
                new Vector3d(80, 35, 97),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //оконная рама (нижний брусок низ)
        OneTriangleModel = new TriangleModel(new Vector3d(-83,32,97),
                new Vector3d(83,32,97),
                new Vector3d(83, 32, 100),
                new Vector3d(-83, 32, 100),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //оконная рама (нижний брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-83,32,97),
                new Vector3d(-83,35,97),
                new Vector3d(83, 35, 97),
                new Vector3d(83, 32, 97),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);


        //стена с окном(низ)
        OneTriangleModel = new TriangleModel(new Vector3d(-90,0,100),
                new Vector3d(-90,35,100),
                new Vector3d(90, 35, 100),
                new Vector3d(90, 0, 100),
                MaterialType.STEEL);
        AllTriangleModels.add(OneTriangleModel);
        //стена с окном(верх)
        OneTriangleModel = new TriangleModel(new Vector3d(-90,85,100),
                new Vector3d(-90,100,100),
                new Vector3d(90, 100, 100),
                new Vector3d(90, 85, 100),
                MaterialType.STEEL);
        AllTriangleModels.add(OneTriangleModel);
        //стена с окном(лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-90,35,100),
                new Vector3d(-90,85,100),
                new Vector3d(-80, 85, 100),
                new Vector3d(-80, 35, 100),
                MaterialType.STEEL);
        AllTriangleModels.add(OneTriangleModel);
        //стена с окном(право)
        OneTriangleModel = new TriangleModel(new Vector3d(80,25,100),
                new Vector3d(80,85,100),
                new Vector3d(90, 85, 100),
                new Vector3d(90, 35, 100),
                MaterialType.STEEL);
        AllTriangleModels.add(OneTriangleModel);
        //передняя часть тумбы
        OneTriangleModel = new TriangleModel(new Vector3d(-15, 0 ,45),
                new Vector3d(-15, 40,45),
                new Vector3d(15, 40, 45),
                new Vector3d(15, 0,45),
                MaterialType.WOOD);
        AllTriangleModels.add(OneTriangleModel);
        //правая часть тумбы
        OneTriangleModel = new TriangleModel(new Vector3d(15,0,45),
                new Vector3d(15,40, 45),
                new Vector3d(15,40,75),
                new Vector3d(15, 0, 75),
                MaterialType.WOOD);
        AllTriangleModels.add(OneTriangleModel);
        //задняя часть тумбы
        OneTriangleModel = new TriangleModel(new Vector3d(15, 0, 75),
                new Vector3d(15, 40,75),
                new Vector3d(-15,40,75),
                new Vector3d(-15,0,75),
                MaterialType.WOOD);
        AllTriangleModels.add(OneTriangleModel);
        //левая часть тумбы
        OneTriangleModel = new TriangleModel(new Vector3d(-15, 0 ,75),
                new Vector3d(-15,40,75),
                new Vector3d(-15,40,45),
                new Vector3d(-15, 0, 45),
                MaterialType.WOOD);
        AllTriangleModels.add(OneTriangleModel);
        //крышка тумбы
        OneTriangleModel = new TriangleModel(new Vector3d(-15,40,45),
                new Vector3d(-15,40,75),
                new Vector3d(15,40,75),
                new Vector3d(15, 40,45),
                MaterialType.WOOD);
        AllTriangleModels.add(OneTriangleModel);



        //рамка для левого зеркала (нижний брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-14.25, 40, 66),
                new Vector3d(-14.25, 41, 66),
                new Vector3d(-13.8, 41, 65.2),
                new Vector3d(-13.8, 40, 65.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для левого зеркала (нижний брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-13.8, 40, 65.2),
                new Vector3d(-13.8, 41, 65.2),
                new Vector3d(-6.5, 41, 69.12),
                new Vector3d(-6.5, 40, 69.12),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для левого зеркала (нижний брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(-6.5, 40, 69.12),
                new Vector3d(-6.5, 41, 69.12),
                new Vector3d(-7, 41, 70),
                new Vector3d(-7, 40, 70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для левого зеркала (нижний брусок верх)
        OneTriangleModel = new TriangleModel(new Vector3d(-14.25, 41, 66),
                new Vector3d(-7, 41, 70),
                new Vector3d(-6.5, 41, 69.12),
                new Vector3d(-13.8, 41, 65.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для левого зеркала (левый брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-14.25, 41, 66),
                new Vector3d(-14.25, 59, 66),
                new Vector3d(-13.8, 59, 65.2),
                new Vector3d(-13.8, 41, 65.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для левого зеркала (левый брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-13.8, 41, 65.2),
                new Vector3d(-13.8, 59, 65.2),
                new Vector3d(-12.9, 59, 65.66),
                new Vector3d(-12.9, 41, 65.66),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для левого зеркала (левый брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(-12.9, 41, 65.66),
                new Vector3d(-12.9, 59, 65.66),
                new Vector3d(-13.36, 59, 66.5),
                new Vector3d(-13.36, 41, 65.66),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для левого зеркала (правый брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-7.87, 41, 69.52),
                new Vector3d(-7.87, 59, 69.52),
                new Vector3d(-7.4, 59, 68.62),
                new Vector3d(-7.4, 41, 68.62),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для левого зеркала (правый брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-7.4, 41, 68.62),
                new Vector3d(-7.4, 59, 68.62),
                new Vector3d(-6.5, 59, 69.12),
                new Vector3d(-6.5, 41, 69.12),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для левого зеркала (правый брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(-6.5, 41, 69.12),
                new Vector3d(-6.5, 59, 69.12),
                new Vector3d(-7, 59, 70),
                new Vector3d(-7, 41, 70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для левого зеркала (верхний брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-14.25, 59, 66),
                new Vector3d(-14.25, 60, 66),
                new Vector3d(-13.8, 60, 65.2),
                new Vector3d(-13.8, 59, 65.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для левого зеркала (верхний брусок низ)
        OneTriangleModel = new TriangleModel(new Vector3d(-13.8, 59, 65.2),
                new Vector3d(-6.5, 59, 69.12),
                new Vector3d(-7, 59, 70),
                new Vector3d(-14.25, 59, 66),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для левого зеркала (верхний брусок верх)
        OneTriangleModel = new TriangleModel(new Vector3d(-13.8, 60, 65.2),
                new Vector3d(-14.25, 60, 66),
                new Vector3d(-7, 60, 70),
                new Vector3d(-6.5, 60, 69.12),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для левого зеркала (верхний брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-13.8, 59, 65.2),
                new Vector3d(-13.8, 60, 65.2),
                new Vector3d(-6.5, 60, 69.12),
                new Vector3d(-6.5, 59, 69.12),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для левого зеркала (верхний брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(-6.5, 59, 69.12),
                new Vector3d(-6.5, 60, 69.12),
                new Vector3d(-7, 60, 70),
                new Vector3d(-7, 59, 70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для левого зеркала (верхний брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-14.25, 59, 66),
                new Vector3d(-14.25, 60, 66),
                new Vector3d(-13.8, 60, 65.2),
                new Vector3d(-13.8, 59, 65.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //левое зеркало
        OneTriangleModel = new TriangleModel(new Vector3d(-7, 40, 70),
                new Vector3d(-14.25, 40, 66),
                new Vector3d(-14.25, 60, 66),
                new Vector3d(-7, 60, 70),
                MaterialType.MIRROR);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для правого зеркала (нижний брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(7, 40, 70),
                new Vector3d(7, 41, 70),
                new Vector3d(6.5, 41, 69.12),
                new Vector3d(6.5, 40, 69.12),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (нижний брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(6.5, 40, 69.12),
                new Vector3d(6.5, 41, 69.12),
                new Vector3d(13.8, 41, 65.2),
                new Vector3d(13.8, 40, 65.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (нижний брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(13.8, 40,65.2),
                new Vector3d(13.8, 41,65.2),
                new Vector3d(14.25, 41, 66),
                new Vector3d(14.25, 40, 66),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (нижний брусок верх)
        OneTriangleModel = new TriangleModel(new Vector3d(6.5, 41, 69.12),
                new Vector3d(7, 41, 70),
                new Vector3d(14.25, 41, 66),
                new Vector3d(13.8, 41, 65.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (левый брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(7, 41, 70),
                new Vector3d(7, 59, 70),
                new Vector3d(6.5, 59, 69.12),
                new Vector3d(6.5, 41, 69.12),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (левый брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(6.5, 41, 69.12),
                new Vector3d(6.5, 59, 69.12),
                new Vector3d(7.4, 59, 68.62),
                new Vector3d(7.4, 41, 68.62),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (левый брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(7.4, 41, 68.62),
                new Vector3d(7.4, 59, 68.62),
                new Vector3d(7.87, 59, 69.52),
                new Vector3d(7.87, 41, 69.52),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для правого зеркала (правый брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(13.36, 41, 66.5),
                new Vector3d(13.36, 59, 66.5),
                new Vector3d(12.9, 59, 65.66),
                new Vector3d(12.9, 41, 65.66),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (правый брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(12.9, 41, 65.66),
                new Vector3d(12.9, 59, 65.66),
                new Vector3d(13.8, 59, 65.2),
                new Vector3d(13.8, 41, 65.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (правый брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(13.8, 41, 65.2),
                new Vector3d(13.8, 59, 65.2),
                new Vector3d(14.25, 59, 66),
                new Vector3d(14.25, 41, 66),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для правого зеркала (верхний брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(14.25, 59, 66),
                new Vector3d(14.25, 60, 66),
                new Vector3d(13.8, 60, 65.2),
                new Vector3d(13.8, 59, 65.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (верхний брусок низ)
        OneTriangleModel = new TriangleModel(new Vector3d(6.5, 59, 69.12),
                new Vector3d(13.8, 59, 65.2),
                new Vector3d(14.25, 59, 66),
                new Vector3d(7, 59, 70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (верхний брусок верх)
        OneTriangleModel = new TriangleModel(new Vector3d(6.5, 60, 69.12),
                new Vector3d(7, 59, 70),
                new Vector3d(14.25, 59, 66),
                new Vector3d(13.8, 59, 65.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (верхний брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(6.5, 59, 69.12),
                new Vector3d(6.5, 60, 69.12),
                new Vector3d(13.8, 60, 65.2),
                new Vector3d(13.8, 59, 65.2),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (верхний брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(13.8, 59, 65.2),
                new Vector3d(13.8, 60, 65.2),
                new Vector3d(14.25, 60, 66),
                new Vector3d(14.25, 59, 66),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для правого зеркала (верхний брусок лево)
        OneTriangleModel = new TriangleModel( new Vector3d(7, 59, 70),
                new Vector3d(7, 60, 70),
                new Vector3d(6.5, 60, 69.12),
                new Vector3d(6.5, 59, 69.12),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //правое зеркало
        OneTriangleModel = new TriangleModel(new Vector3d(7, 40, 70),
                new Vector3d(14.25, 40, 66),
                new Vector3d(14.25, 60, 66),
                new Vector3d(7, 60, 70),
                MaterialType.MIRROR);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для центрального зеркала (нижний брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-6, 40, 70),
                new Vector3d(-6, 41, 70),
                new Vector3d(-6, 41, 69),
                new Vector3d(-6, 40, 69),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для центрального зеркала (нижний брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(6, 40, 70),
                new Vector3d(6, 40, 69),
                new Vector3d(6, 41, 69),
                new Vector3d(6, 41, 70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для центрального зеркала ( нижний брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-6, 40, 70),
                new Vector3d(-6, 41, 70),
                new Vector3d(6, 41, 69),
                new Vector3d(6, 40, 69),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для центрального зеркала (нижний брусок верх)
        OneTriangleModel = new TriangleModel(new Vector3d(-6, 41, 70),
                new Vector3d(6, 41, 70),
                new Vector3d(6, 41, 69),
                new Vector3d(-6, 41, 69),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для центрального зеркала (левый брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-6, 41, 70),
                new Vector3d(-6, 59, 70),
                new Vector3d(-6, 59, 69),
                new Vector3d(-6, 41, 69),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для центрального зеркала (левый брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-6, 41, 69),
                new Vector3d(-6, 59, 69),
                new Vector3d(-5, 59, 69),
                new Vector3d(-5, 41, 69),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для центрального зеркала (левый брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(-5, 41, 70),
                new Vector3d(-5, 41, 69),
                new Vector3d(-5, 59, 69),
                new Vector3d(-5, 59, 70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для центрального зеркала (правый брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(5, 41, 70),
                new Vector3d(5, 59, 70),
                new Vector3d(5, 59, 69),
                new Vector3d(5, 41, 69),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для центрального зеркала (правый брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(5, 41, 69),
                new Vector3d(5, 59, 69),
                new Vector3d(6, 59, 69),
                new Vector3d(6, 41, 69),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для центрального зеркала (правый брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(6, 41, 70),
                new Vector3d(6, 41, 69),
                new Vector3d(6, 59, 69),
                new Vector3d(6, 59, 70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //рамка для центрального зеркала (верхний брусок лево)
        OneTriangleModel = new TriangleModel(new Vector3d(-6, 59, 70),
                new Vector3d(-6, 60, 70),
                new Vector3d(-6, 60, 69),
                new Vector3d(-6, 59, 69),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для центрального зеркала (верхний брусок верх)
        OneTriangleModel = new TriangleModel(new Vector3d(-6, 60, 69),
                new Vector3d(-6, 60, 70),
                new Vector3d(6, 60, 70),
                new Vector3d(6, 60, 69),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для центрального зеркала (верхний брусок перед)
        OneTriangleModel = new TriangleModel(new Vector3d(-6, 59, 69),
                new Vector3d(-6, 60, 69),
                new Vector3d(6, 60, 69),
                new Vector3d(6, 59, 69),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для центрального зеркала (верхний брусок низ)
        OneTriangleModel = new TriangleModel(new Vector3d(-6, 59, 70),
                new Vector3d(-6, 60, 70),
                new Vector3d(-6, 60, 69),
                new Vector3d(-6, 59, 69),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);
        //рамка для центрального зеркала (верхний брусок право)
        OneTriangleModel = new TriangleModel(new Vector3d(6, 59, 69),
                new Vector3d(6, 60, 69),
                new Vector3d(6, 60, 70),
                new Vector3d(6, 59, 70),
                MaterialType.BROWNWOOD);
        AllTriangleModels.add(OneTriangleModel);

        //центральное зеркало
        OneTriangleModel = new TriangleModel(new Vector3d(6,40,70),
                new Vector3d(-6, 40, 70),
                new Vector3d(-6 , 60, 70),
                new Vector3d(6, 60, 70),
                MaterialType.MIRROR);
        AllTriangleModels.add(OneTriangleModel);



        //4 зеркало
        //OneTriangleModel = new TriangleModel(new Vector3d(11.25, 40,67),
          //      new Vector3d(5,40,70),
            //    new Vector3d(5, 60,70),
              //  new Vector3d(11.25, 60,67),
                //MaterialType.MIRROR);
        //AllTriangleModels.add(OneTriangleModel);

        //5е зеркало
       //OneTriangleModel = new TriangleModel(new Vector3d(-5, 60, 70),
        //new Vector3d(-5, 80,62),
          //      new Vector3d(5,80,62),
            //    new Vector3d(5,60,70),
              //  MaterialType.MIRROR);
        //AllTriangleModels.add(OneTriangleModel);



    }

    public void RenderScene(Display3D display) {
        for (int j = 0; j < height; ++j) {//по y
            for (int i = 0; i < width; ++i) {//по х
                RenderOnePixel(i, j);
            }
        }
    }




    public void RenderOnePixel( int i, int j){
        double x = (2 * (i + 0.5) / width - 1) * Math.tan(fov / 2) * width / height;
        double y = -(2 * (j + 0.5) / height - 1) * Math.tan(fov / 2);
        Vector3d dir = (new Vector3d(x , y , dirZ )).normalize();

        Vector3d color_kef = CalculateSpherePixelColor(camera, dir, 0);

        if(color_kef!=null) {
            double max_color_kef = Math.max(color_kef.x, Math.max(color_kef.y, color_kef.z));
            if (max_color_kef > 1)
                color_kef = color_kef.getVectorScaled(1. / max_color_kef);
            Color pixelColor = new Color((int) (255 * Math.max(0, Math.min(1, color_kef.x))),
                    (int) (255 * Math.max(0, Math.min(1, color_kef.y))),
                    (int) (255 * Math.max(0, Math.min(1, color_kef.z))));
            Display.AddPointOnDisplayRT(i,j,pixelColor);
        }
        else
            Display.AddPointOnDisplayRT(i,j,null);
    }


    public  Vector3d CalculateSpherePixelColor(Vector3d orig, Vector3d dir, int depth){
        SceneIntersect scene = new SceneIntersect(orig,dir,AllSpheres,AllTriangleModels);

        if (depth<=maxDepth && scene.isIntersect) {
            Vector3d reflect_dir = reflect(dir, scene.N);
            Vector3d refract_dir = refract(dir, scene.N, scene.material.refractive).normalize();
            Vector3d reflect_orig = reflect_dir.getScalar(scene.N) < 0 ?
                    scene.hit.getSubtraction(scene.N.getVectorScaled(0.001)) :
                    scene.hit.getAddition(scene.N.getVectorScaled(0.001));
            Vector3d refract_orig = refract_dir.getScalar(scene.N) < 0 ?
                    scene.hit.getSubtraction(scene.N.getVectorScaled(0.001)) :
                    scene.hit.getAddition(scene.N.getVectorScaled(0.001));
            Vector3d reflect_color = CalculateSpherePixelColor(
                    reflect_orig,
                    reflect_dir,
                    depth+1);
            Vector3d refract_color = CalculateSpherePixelColor(
                    refract_orig,
                    refract_dir,
                    depth+1);

            double diffuse_light_intensity = 0,
                    specular_light_intensity = 0;

            for (int i=0; i<AllLights.size(); i++) {
                Vector3d light_dir = (AllLights.elementAt(i).position.getSubtraction(scene.hit)).normalize();
                double light_dist = (AllLights.elementAt(i).position.getSubtraction(scene.hit)).getModule();

                Vector3d shadow_orig = (light_dir.getScalar(scene.N) < 0) ?
                        scene.hit.getSubtraction(scene.N.getVectorScaled(0.001)) :
                        scene.hit.getAddition(scene.N.getVectorScaled(0.001)); // checking if the point lies in the shadow of the lights[i]

                SceneIntersect shadow = new SceneIntersect(
                        shadow_orig,
                        light_dir,
                        AllSpheres,
                        AllTriangleModels);
                if (shadow.isIntersect){
                    double shadow_l = (shadow.hit.getSubtraction(shadow_orig)).getModule();
                    if(shadow_l<light_dist)
                        continue;
                }

                diffuse_light_intensity += AllLights.elementAt(i).intensity * Math.max(0, light_dir.getScalar(scene.N));
                specular_light_intensity += Math.pow(
                        Math.max(0, reflect(light_dir,scene.N).getScalar(dir)),
                        scene.material.specularExponent)
                        *AllLights.elementAt(i).intensity;

            }

            Vector3d result_0 = scene.material.color.getVectorScaled(diffuse_light_intensity * scene.material.albedo[0]);
            Vector3d result_1 = (new Vector3d(1.,1.,1.)).getVectorScaled(specular_light_intensity * scene.material.albedo[1]);
            if(reflect_color!=null) {
                Vector3d result_2 = reflect_color.getVectorScaled(scene.material.albedo[2]);
                if (refract_color != null) {
                    Vector3d result_3 = refract_color.getVectorScaled(scene.material.albedo[3]);
                    return result_0.getAddition(result_1.getAddition(result_2.getAddition(result_3)));// sphere color//resultColor
                }
                return result_0.getAddition(result_1.getAddition(result_2));// sphere color//resultColor
            }
            return result_0.getAddition(result_1);// sphere color//resultColor
        }
        return null;
    }


    public  Vector3d reflect(Vector3d i, Vector3d n) {
        double I_N=i.getScalar(n);
        return i.getSubtraction(n.getVectorScaled(2.f*I_N));
    }

    public  Vector3d refract(Vector3d i, Vector3d n, double etaT) {
        double etaI = 1, buf;
        double cosi = -Math.max(-1, Math.min(1,i.getScalar(n)));
        Vector3d n_new = n;
        if(cosi<0){
            cosi = -cosi;
            buf=etaT; etaT=etaI; etaI=buf;
            n_new = n.getVectorScaled(-1);
        }

        double eta = etaI/etaT;
        double k = 1 - eta*eta*(1 - cosi*cosi);
        return k<0 ?
                new Vector3d(0,0,0) :
                (i.getVectorScaled(eta)).getAddition(n_new.getVectorScaled(eta*cosi - Math.sqrt(k)));
    }

}
