# steps

A controller enabling a paginated layout style in either a Dialog or a full-screen Activity.

![Steps Sample](https://github.com/mattsilber/steps/raw/master/steps.gif)

# Installation

```
    repositories {
        jcenter()
    }

    dependencies {
        compile('com.guardanis:steps:1.0.0')
    }
```


# Usage

The concept is pretty simple: attach StepModule (a View controller) to a StepController, which in turn handles the navigation between them.

The StepController will inflate the next StepModule as it's needed (using the layout resource ID you pass into the constructor) and then call the **setup(StepController, View)** method with the View it inflates. It's the *setup* method where you should build your View information (e.g. setting text, images, etc.).

For convenience, there is a base View to inflate, called *R.layout.step__base_module* which contains a centered ImageView, a title TextView, and a description TextView (vertically in that order). The attributes for each are entirely customizable via dimens/colors/strings.

If you want to use your own resources, make sure your root View is a DraggableLinearLayout (supplied with the library) if you want the content to be swipe-able.


The following is an example Activity showing how to mimich the GIF above:

```
public class MainActivity extends ActionBarActivity implements StepController.StepEventListener {

    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_main);

        findViewById(R.id.step__test_textview).setOnClickListener((v) -> showDialog());
    }

    private void showDialog(){
        View v = getLayoutInflater().inflate(R.layout.step__content, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setView(v);

        dialog = builder.show();

        StepController controller = new StepController(v, getModules(), this);
    }

    private List<StepModule> getModules(){
        List<StepModule> modules = new ArrayList<StepModule>();

        for(int i = 0; i < 5; i++)
            modules.add(buildFakeModule());

        return modules;
    }

    private StepModule buildFakeModule(){
        return new StepModule(R.layout.step__base_module) {
            @Override
            protected void setup(StepController controller, View content) {
                ((ImageView) content.findViewById(R.id.step__base_model_image)).setImageResource(R.drawable.cu__ptr_pulling_image);
                ((TextView) content.findViewById(R.id.step__base_model_title)).setText("Some title");
                ((TextView) content.findViewById(R.id.step__base_model_description)).setText(generateRandomDescription());
            }
        };
    }

    private String generateRandomDescription(){
        String random = "This is random text ";

        String toReturn = "";
        for(int i = 0; i < new Random().nextInt(20) + 3; i++)
            toReturn += random;

        return toReturn;
    }

    @Override
    public void onFinished() {
        dismissDialog();
    }

    @Override
    public void onSkipped() {
        dismissDialog();
    }

    private void dismissDialog(){
        try{
            dialog.dismiss();
        }
        catch(Exception e){ }
    }
}
```


