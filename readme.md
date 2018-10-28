# steps

A controller enabling a paginated layout style in either a Dialog or a full-screen Activity.

![Steps Sample](https://github.com/mattsilber/steps/raw/master/steps.gif)

# Installation

```groovy
repositories {
    jcenter()
}

dependencies {
    implementation 'com.guardanis:steps:2.0.0'
}
```

# Usage

The concept is pretty simple: attach StepModule (a View controller) to a StepController, which in turn handles the navigation between them.

The StepController will inflate the next StepModule as it's needed (using the layout resource ID you pass into the constructor) and then call the **setup(StepController, View)** method with the View it inflates. It's the *setup* method where you should build your View information (e.g. setting text, images, etc.).

For convenience, there is a base View to inflate, called *R.layout.step__base_module* which contains a centered ImageView, a title TextView, and a description TextView (vertically in that order). The attributes for each are entirely customizable via dimens/colors/strings.

If you want to use your own resources, make sure your root View is a DraggableLinearLayout (supplied with the library) if you want the content to be swipe-able.


### Example from the Gif

```java
public class MainActivity extends ActionBarActivity implements StepController.StepEventListener {

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);

        new Handler(Looper.getMainLooper())
                .postDelayed(() -> showDialog(), 300);
    }

    private void showDialog(){
        new StepDialogBuilder(this)
                .show(this, getModules());
    }

    private List<StepModule> getModules(){
        List<StepModule> modules = new ArrayList<StepModule>();

        for(int i = 0; i < 5; i++)
            modules.add(new BaseStepModule(R.drawable.some_image, "Some awesome title", "This is some text This is some more text And even more text And more"));

        return modules;
    }

    private String generateRandomDescription(){
        String random = "";

        String toReturn = "";
        for(int i = 0; i < new Random().nextInt(20) + 3; i++)
            toReturn += random;

        return toReturn;
    }

    @Override
    public void onFinished() {
        new Handler(Looper.getMainLooper())
                .postDelayed(() -> showDialog(), 1000);
    }

    @Override
    public void onSkipped() {
        new Handler(Looper.getMainLooper())
                .postDelayed(() -> showDialog(), 1000);
    }
}
```

So, basically all you need to do is call

```java
new StepDialogBuilder(this)
    .show(this, getModules());
```

And just pass in a list of Modules and an EventListener (which can be null if you don't want to do anything after).

### BaseStepModule

The BaseModule helper class can set the image resource, title, and description values for the Views found in the *step__base_module* layout file. Just pass in the arguments to its constructor and it handles the rest. e.g.

```java
new BaseStepModule(R.drawable.some_image, "Some title", "Some description"));
```


