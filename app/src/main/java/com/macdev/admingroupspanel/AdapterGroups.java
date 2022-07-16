package com.macdev.admingroupspanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterGroups extends RecyclerView.Adapter<AdapterGroups.ViewHolder>{


    String groupUrlDb;
    private String docID;
     DialogPlus dialogPlus;


    private List<ModelGroups> modelGroups;
    Context context;


    public AdapterGroups(List<ModelGroups> modelGroups, Context context) {
        this.modelGroups = modelGroups;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.group_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final ModelGroups currentGroup = modelGroups.get(position);


        String url = modelGroups.get(position).getGroupImage();
        Glide.with(context)
                .load(url) // image url
                .placeholder(R.drawable.ic_image_24) // any placeholder to load at start
                .error(R.drawable.ic_image_24)  // any image in case of error
                .override(100, 100) // resizing
                .centerCrop()
                .into(holder.groupImage);
        holder.groupTitleText.setText(modelGroups.get(position).getGroupName());
        holder.groupCategoryText.setText(modelGroups.get(position).getGroupCategory());

        holder.groupLinkHolder.setText(modelGroups.get(position).getGroupLink());


        holder.editGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 dialogPlus = DialogPlus.newDialog(holder.groupImage.getContext())
                        .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(R.layout.edit_popup))
                        .setExpanded(true,800)
                        .create();


                View view1 = dialogPlus.getHolderView();
                EditText groupName = view1.findViewById(R.id.group_name_editText);
                EditText groupLink = view1.findViewById(R.id.group_link_editText);
                EditText groupCategory = view1.findViewById(R.id.group_category_editText);
                EditText groupImageLink = view1.findViewById(R.id.group_image_link_editText);

                Button buttonUpdate = view1.findViewById(R.id.update_group);

                groupName.setText(currentGroup.getGroupName());
                groupLink.setText(currentGroup.getGroupLink());
                groupCategory.setText(currentGroup.getGroupCategory());
                groupImageLink.setText(currentGroup.getGroupImage());


                dialogPlus.show();

                buttonUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("GroupName", groupName.getText().toString());
                        map.put("GroupCategory", groupCategory.getText().toString());
                        map.put("GroupImage", groupImageLink.getText().toString());
                        map.put("GroupLink", groupLink.getText().toString());

                        FirebaseFirestore.getInstance().collection("Groups")
                                .whereEqualTo("GroupImage", currentGroup.groupImage)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot document : task.getResult()){
                                                docID = document.getId();

                                            }
                                            FirebaseFirestore.getInstance().collection("Groups").document(docID).set(map)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();

                                                            dialogPlus.dismiss();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    dialogPlus.dismiss();
                                                }
                                            });
                                        }
                                    }
                                });
                    }
                });
            }
        });



        holder.deleteGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore.getInstance().collection("Groups")
                        .whereEqualTo("GroupImage", currentGroup.groupImage)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        docID = document.getId();

                                    }
                                    FirebaseFirestore.getInstance().collection("Groups").document(docID)
                                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(context, "Error Deleting", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            }
                        });

            }
        });





    }

    @Override
    public int getItemCount() {
        return modelGroups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public TextView groupTitleText, groupCategoryText, groupLinkHolder;
        public ShapeableImageView groupImage;
        public LinearLayout joinBtnLayout;
        public ImageView moreIcon;
        public CardView cardViewGroupList;
        public Button editGroupBtn, deleteGroupBtn;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            this.groupTitleText = (TextView) itemView.findViewById(R.id.text_group_title);
            this.groupCategoryText = (TextView) itemView.findViewById(R.id.text_group_category);
            this.groupImage = (ShapeableImageView) itemView.findViewById(R.id.group_Image);
            this.moreIcon= (ImageView) itemView.findViewById(R.id.more_btn);
            this.editGroupBtn= (Button) itemView.findViewById(R.id.edit_btn_group);
            this.deleteGroupBtn= (Button) itemView.findViewById(R.id.delete_btn_group);
            this.groupLinkHolder = (TextView) itemView.findViewById(R.id.groupLinkTextHidden);
            this.joinBtnLayout = (LinearLayout) itemView.findViewById(R.id.join_layout);
            this.cardViewGroupList = (CardView) itemView.findViewById(R.id.card_view_group_list);







        }



    }
}
