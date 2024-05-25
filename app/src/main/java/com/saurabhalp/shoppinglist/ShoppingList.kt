import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id :Int,
    var name: String,
    var quantity: Int,
    var isEditing : Boolean = false,
)

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ShoppingList() {
    var sItem by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuan by remember { mutableStateOf("") }
    Column(
        Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally)
        )
        { Text("Add Item") }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(20.dp))
        {
            items(sItem) {
               item ->
                if(item.isEditing){
                    ShoppingListEditor(item = item, onEditingComplete = {
                        editedName, editedQuantity ->
                        sItem = sItem.map { it.copy(isEditing = false) }
                        val editedItem = sItem.find { it.id ===item.id }
                        editedItem?.let {
                            it.name = editedName
                            it.quantity=editedQuantity

                        }
                    }
                     )
                }
                else {
                    ShoppingListItems(item=item, onEditClick = {
                        sItem = sItem.map{it.copy(isEditing = it.id==item.id)}
                    }, onDeleteClick = {sItem=sItem-item})
                }

            }
        }
    }
    if (showDialog) {
       AlertDialog(
           onDismissRequest = {showDialog = false},
           confirmButton = {
                           Row ( modifier =Modifier.fillMaxWidth().padding(8.dp),
                               horizontalArrangement = Arrangement.SpaceBetween ){
                               Button(onClick = {
                                    if(itemName.isNotEmpty()){
                                        val newItem = ShoppingItem(
                                            id = sItem.size+1,
                                            name = itemName,
                                            quantity = itemQuan.toInt(),
                                            isEditing = false,

                                        )
                                        sItem = sItem+newItem
                                        showDialog = false
                                        itemName = ""
                                        itemQuan= ""
                                    }
                               }){
                                   Text("Add")
                               }
                               Button(onClick = { showDialog = false
                               })
                               {
                                   Text("Cancel")
                               }

                           }
           },
           title = { Text("Add Shopping Items") },
           text = {
               Column {
                   OutlinedTextField(value =itemName,
                       onValueChange ={itemName = it},
                       singleLine = true,
                       modifier = Modifier.fillMaxWidth().padding(8.dp),


                       )

                   OutlinedTextField(value = itemQuan,
                       onValueChange ={itemQuan = it},
                       singleLine = true,
                       modifier = Modifier.fillMaxWidth().padding(8.dp),


                       )

               }
           })
    }
}
@Composable
fun ShoppingListItems(
    item: ShoppingItem,
               onEditClick: ()->Unit,
               onDeleteClick: ()->Unit,
           ){
               Row (
                   Modifier.padding(8.dp).fillMaxWidth().border(
                       border = BorderStroke(5.dp, Color.Gray),
                       shape = RoundedCornerShape(20)
                   ),horizontalArrangement = Arrangement.SpaceBetween,
                   verticalAlignment = Alignment.CenterVertically,
               )

               {
                   Text(text = item.name, modifier = Modifier.padding(8.dp))

                   Text(text = "Qty : "+item.quantity.toString(), modifier = Modifier.padding(8.dp))

                    Row(modifier = Modifier.padding(8.dp)) {
                        IconButton(onClick = onEditClick){
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                        }
                        IconButton(onClick = onDeleteClick){
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                    }
               }

           }
@Composable
fun ShoppingListEditor(item: ShoppingItem, onEditingComplete :(String,Int)->Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuan by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp).background(Color.White)){
        Column(modifier = Modifier.padding(8.dp)) {
            BasicTextField(value = editedName, onValueChange = {editedName=it},
                singleLine = true,
                modifier = Modifier.wrapContentSize())

            BasicTextField(value = editedQuan, onValueChange = {editedQuan=it},
                singleLine = true,
                modifier = Modifier.wrapContentSize())

        }
            Button(onClick = {
                isEditing = false
                onEditingComplete(editedName, editedQuan.toIntOrNull() ?: 1)

            }) {
                Text("Save")
                Modifier.padding(8.dp)
            }
    }

}







