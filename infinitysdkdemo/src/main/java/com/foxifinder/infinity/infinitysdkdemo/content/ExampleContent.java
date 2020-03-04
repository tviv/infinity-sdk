package com.foxifinder.infinity.infinitysdkdemo.content;

import androidx.fragment.app.Fragment;

import com.foxifinder.infinity.infinitysdkdemo.impl.ExampleButtonDetailFragment;
import com.foxifinder.infinity.infinitysdkdemo.impl.ExampleInitDetailFragment;
import com.foxifinder.infinity.sdk.InfinitySDK;
import com.foxifinder.infinity.sdk.InfinitySDK.CommandCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<ExampleItem> ITEMS = new ArrayList<ExampleItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, ExampleItem> ITEM_MAP = new HashMap<String, ExampleItem>();

    private static void addItem(ExampleItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class ExampleItem {
        public final String id;
        public final String content;
        public final String details;
        public final String demoText;
        public final Class<? extends Fragment> fragmentClass;
        public final ExampleButtonItem[] buttons;

        public ExampleItem(String id,
                           String content,
                           String details,
                           String demoText,
                           Class<? extends Fragment> fragmentClass,
                           ExampleButtonItem[] buttons
        ) {
            this.id = id;
            this.content = content;
            this.details = details;
            this.demoText = demoText;
            this.fragmentClass = fragmentClass;
            this.buttons = buttons;
        }

        @Override
        public String toString() {
            return content;
        }
    }

    public interface TestCode {
        public void testCode(InfinitySDK sdk, CommandCallback cb, String params);
    }

    public static class ExampleButtonItem {
        public String title;
        public TestCode testCode;

        ExampleButtonItem(String title, TestCode testCode) {
            this.title = title;
            this.testCode = testCode;
        }
    }



    static {
        addItem(new ExampleItem("init",
                "Initialization",
                "",
                "InfinitySDK sdk = new InfinitySDK(context);",
                ExampleInitDetailFragment.class,
                null
        ));

        addItem(new ExampleItem(
                "serv_version",
                "Get Service version",
                "",
                "String result = sdk.getServiceVersion();",
                ExampleButtonDetailFragment.class,
                new ExampleButtonItem[] {
                        new ExampleButtonItem("Get Service Version", new TestCode(){
                            @Override
                            public void testCode(InfinitySDK sdk, CommandCallback cb, String params) {
                                String res = sdk.getServiceVersion();
                                if (res != null) cb.onSuccess(res, null);
                                else cb.onError("Error getting data", null);
                            }
                        })

                }
        ));

        addItem(new ExampleItem(
                "airplane",
                "Set airplane mode",
                "",
                "sdk.setAirplaneMode(true, cb)",
                ExampleButtonDetailFragment.class,
                new ExampleButtonItem[] {
                        new ExampleButtonItem("ON", new TestCode(){
                            @Override
                            public void testCode(InfinitySDK sdk, CommandCallback cb, String params) {
                                sdk.commands.setAirplaneMode(true, cb);
                            }
                        }),
                        new ExampleButtonItem("OFF", new TestCode(){
                            @Override
                            public void testCode(InfinitySDK sdk, CommandCallback cb, String params) {
                                sdk.commands.setAirplaneMode(false, cb);
                            }
                        })

                }
                ));

        addItem(new ExampleItem(
                "mobile_data",
                "Set mobile data enabled",
                "",
                "sdk.setMobileDataEnabled(true, cb)",
                ExampleButtonDetailFragment.class,
                new ExampleButtonItem[] {
                        new ExampleButtonItem("ON", new TestCode(){
                            @Override
                            public void testCode(InfinitySDK sdk, CommandCallback cb, String params) {
                                sdk.commands.setMobileDataEnabled(true, cb);
                            }
                        }),
                        new ExampleButtonItem("OFF", new TestCode(){
                            @Override
                            public void testCode(InfinitySDK sdk, CommandCallback cb, String params) {
                                sdk.commands.setMobileDataEnabled(false, cb);
                            }
                        })

                }
        ));

    }


}
