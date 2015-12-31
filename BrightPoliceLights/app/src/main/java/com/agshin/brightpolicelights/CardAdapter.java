package com.agshin.brightpolicelights;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.agshin.brightpolicelights.colorpicker.ColorPickerView;
import com.agshin.brightpolicelights.colorpicker.OnColorSelectedListener;
import com.agshin.brightpolicelights.colorpicker.builder.ColorPickerClickListener;
import com.agshin.brightpolicelights.colorpicker.builder.ColorPickerDialogBuilder;
import com.agshin.brightpolicelights.comboseekbar.ComboSeekBar;

import java.util.ArrayList;
import java.util.Arrays;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    ArrayList<PatternElement> elements;
    private Context context;
    private RecyclerView recyclerView;
//    private ArrayList<Pattern> patterns;
//    private int patternId;

    public CardAdapter(ArrayList<PatternElement> elements, Context context, RecyclerView recyclerView) {
        super();
        this.context = context;
        this.elements = elements;
        this.recyclerView = recyclerView;
//        this.patterns = patterns;
//        this.patternId = patternId;
    }
//
//    private void update(){
//        patterns.get(patternId).setElements(elements);
//    }
//
//    public void setPatternId(int pid){
//        this.patternId = pid;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup,final int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_card_item, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(v);



        return viewHolder;
    }

    private void setColor(int position, int color){
        elements.get(position).setColor(color);
//        update();
    }
    private void setDuration(int position, int duration){
        elements.get(position).setDuration(duration);
    }

    public void removeAt(int position) {
        if(elements.size() < 2){
            Snackbar.make(recyclerView,context.getResources().getString(R.string.remove_last_warning), Snackbar.LENGTH_SHORT).show();
            return;
        }
        elements.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, elements.size());
        notifyItemChanged(elements.size() - 1);
    }

    public void addElement(int position, PatternElement element) {
        elements.add(position, element);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, elements.size());

        recyclerView.scrollToPosition(elements.size() - 1);
    }



    @Override
    public void onBindViewHolder(final ViewHolder viewHolder,final int i) {
        PatternElement item = elements.get(i);
//        viewHolder.tvTime.setText(String.valueOf(item.getDuration()));
        viewHolder.btnColor.setBackgroundColor(item.getColor());
//        viewHolder.rbPause.setChecked(item.getType() == PatternElementType.Pause);
//        viewHolder.rbLight.setChecked(item.getType() == PatternElementType.Light);
        viewHolder.cbIsFlash.setChecked(item.getType() == PatternElementType.Light);

        if(item.getType() == PatternElementType.Pause){
//            viewHolder.btnColor.setEnabled(false);
            viewHolder.btnColor.setText(context.getResources().getString(R.string.pause));
        } else {
//            viewHolder.btnColor.setEnabled(true);
            viewHolder.btnColor.setText(context.getResources().getString(R.string.flash));
        }

        if( i == elements.size() - 1){
            viewHolder.btnNewElement.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnNewElement.setVisibility(View.GONE);
        }

        final String[] ads = {
                "",
                "0.1",
                "",
                "0.2",
                "",
                "0.3",
                "",
                "0.4",
                "",
                "0.5",
                "",
                "0.6",
                "",
                "0.7",
                "",
                "0.8",
                "",
                "0.9",
                "",
                "1",
                "",
                "2",
                "",
                "3 s"};
        viewHolder.csTime.setAdapter(Arrays.asList(ads));
        viewHolder.csTime.setColor(Color.rgb(0, 0, 0));





        ///////////////////////////////////
        final int[] adi = {
                50,
                100,
                150,
                200,
                250,
                300,
                350,
                400,
                450,
                500,
                550,
                600,
                650,
                700,
                750,
                800,
                850,
                900,
                950,
                1000,
                1500,
                2000,
                2500,
                3000};

        int k = 0;
        for(int l = 0; l < adi.length; l++){
            if(item.getDuration() == adi[l]){
                k = l;
                break;
            }
        }

        viewHolder.csTime.setSelection(k);

        viewHolder.csTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    setDuration(i, adi[progress]);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(i);
            }
        });

        viewHolder.btnNewElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(elements.size() >= 99){
                    Snackbar.make(viewHolder.btnNewElement,"More than 100 elements not supported.",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                int dur = 50;
                int col = Color.RED;
                PatternElementType type = PatternElementType.Light;

                if (elements.size() > 0) {
                    PatternElement el = elements.get(elements.size() - 1);
                    dur = el.getDuration();
                    col = el.getColor();
                    type = el.getType() == PatternElementType.Pause ? PatternElementType.Light : PatternElementType.Pause;
                }

                addElement(elements.size(), new PatternElement(dur, type, col));
                viewHolder.btnNewElement.setVisibility(View.GONE);

            }
        });

//        viewHolder.cbIsFlash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    viewHolder.btnColor.setEnabled(false);
//                    setType(i, PatternElementType.Pause);
//                } else {
//                    viewHolder.btnColor.setEnabled(true);
//                    setType(i, PatternElementType.Light);
//                }
//            }
//        });
        viewHolder.cbIsFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewHolder.cbIsFlash.isChecked()) {
//                    viewHolder.btnColor.setEnabled(false);
                    viewHolder.btnColor.setText(context.getResources().getString(R.string.pause));
                    setType(i, PatternElementType.Pause);
                } else {
                    viewHolder.btnColor.setText(context.getResources().getString(R.string.flash));
//                    viewHolder.btnColor.setEnabled(true);
                    setType(i, PatternElementType.Light);
                }
            }
        });

//        viewHolder.rbPause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    viewHolder.btnColor.setEnabled(false);
//                    setType(i, PatternElementType.Pause);
//                } else {
//                    viewHolder.btnColor.setEnabled(true);
//                    setType(i, PatternElementType.Light);
//                }
//            }
//        });
//        viewHolder.rbLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (!isChecked) {
//                    viewHolder.btnColor.setEnabled(false);
//                    setType(i, PatternElementType.Pause);
//                } else {
//                    viewHolder.btnColor.setEnabled(true);
//                    setType(i, PatternElementType.Light);
//                }
//            }
//        });


        ColorDrawable pd = (ColorDrawable) viewHolder.btnColor.getBackground();
        final int clr = pd.getColor();
        viewHolder.btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle(context.getResources().getString(R.string.choose_a_color))
                        .initialColor(clr)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(8)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {

                            }
                        })
                        .setPositiveButton(context.getResources().getString(R.string.ok), new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                viewHolder.btnColor.setBackgroundColor(selectedColor);
                                setColor(i, selectedColor);

                                if (allColors != null) {
                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                    }

                                }
                            }
                        })
                        .setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .build()
                        .show();
            }
        });

        ///////////////////////////////////
    }

    public void setType(int position, PatternElementType type){
        elements.get(position).setType(type);
//        update();
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

//        public RadioButton rbLight;
//        public RadioButton rbPause;
        public CheckBox cbIsFlash;
        public ComboSeekBar csTime;
        public Button btnColor;
        public Button btnNewElement;
        public ImageButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

//            rbLight = (RadioButton) itemView.findViewById(R.id.rbColor);
//            rbPause = (RadioButton) itemView.findViewById(R.id.rbPause);

            cbIsFlash = (CheckBox) itemView.findViewById(R.id.cbIsFlash);
            csTime = (ComboSeekBar) itemView.findViewById(R.id.csTime);
            btnColor = (Button) itemView.findViewById(R.id.btnColor);
            btnNewElement = (Button) itemView.findViewById(R.id.btnNewElement);
            btnDelete = (ImageButton) itemView.findViewById(R.id.btnDelete);
        }


    }
}
