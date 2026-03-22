package com.shaurmagrill.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShaurmaGrillTheme {
                AppRoot()
            }
        }
    }
}

private enum class Screen {
    HOME, CARROT, MEAT
}

private enum class UnitType(val title: String) {
    GRAM("г"),
    TSP("ч.л."),
    TBSP("ст.л.")
}

private data class Ingredient(
    val name: String,
    val amountPer1000g: Double,
    val unitType: UnitType
)

private enum class RecipeType(
    val title: String,
    val inputTitle: String,
    val accentTitle: String,
    val ingredients: List<Ingredient>
) {
    CARROT(
        title = "Корейская морковка",
        inputTitle = "Введите вес моркови",
        accentTitle = "Расчёт специй на морковку",
        ingredients = listOf(
            Ingredient("Соль", 3.0, UnitType.TSP),
            Ingredient("Сахар", 3.0, UnitType.TBSP),
            Ingredient("Уксус", 6.0, UnitType.TBSP),
            Ingredient("Чеснок", 30.0, UnitType.GRAM),
            Ingredient("Кориандр", 1.0, UnitType.TSP),
            Ingredient("Перец чёрный молотый", 1.0, UnitType.TSP),
            Ingredient("Масло", 100.0, UnitType.GRAM)
        )
    ),
    MEAT(
        title = "Маринад мяса",
        inputTitle = "Введите вес мяса",
        accentTitle = "Расчёт маринада на мясо",
        ingredients = listOf(
            Ingredient("Соль", 0.5, UnitType.TSP),
            Ingredient("Лук репчатый", 150.0, UnitType.GRAM),
            Ingredient("Масло", 30.0, UnitType.GRAM),
            Ingredient("Перец чёрный молотый", 0.5, UnitType.TSP),
            Ingredient("Паприка", 1.0, UnitType.TSP),
            Ingredient("Соевый соус", 60.0, UnitType.GRAM)
        )
    )
}

private val AppColors = lightColorScheme(
    primary = Color(0xFF4B2E1E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0C2),
    onPrimaryContainer = Color(0xFF2A160A),
    secondary = Color(0xFFB85C38),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDBCC),
    onSecondaryContainer = Color(0xFF3B180D),
    tertiary = Color(0xFF7A9D54),
    background = Color(0xFFFFFBF7),
    surface = Color.White,
    surfaceVariant = Color(0xFFF5EDE5),
    outline = Color(0xFFD8C8BA),
    error = Color(0xFFB3261E)
)

@Composable
private fun ShaurmaGrillTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColors,
        content = content
    )
}

@Composable
private fun AppRoot() {
    var currentScreen by rememberSaveable { mutableStateOf(Screen.HOME) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (currentScreen) {
            Screen.HOME -> HomeScreen(
                onOpenCarrot = { currentScreen = Screen.CARROT },
                onOpenMeat = { currentScreen = Screen.MEAT }
            )

            Screen.CARROT -> RecipeScreen(
                recipeType = RecipeType.CARROT,
                onBack = { currentScreen = Screen.HOME }
            )

            Screen.MEAT -> RecipeScreen(
                recipeType = RecipeType.MEAT,
                onBack = { currentScreen = Screen.HOME }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    onOpenCarrot: () -> Unit,
    onOpenMeat: () -> Unit
) {
    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {
                    Text(
                        text = "Шаурма Гриль",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterVertically)
        ) {
            HeaderCard()

            MenuButton(
                title = "Корейская морковка",
                subtitle = "Рассчитать специи по весу моркови",
                onClick = onOpenCarrot
            )

            MenuButton(
                title = "Маринад мяса",
                subtitle = "Рассчитать маринад по весу мяса",
                onClick = onOpenMeat
            )

            Text(
                text = "Вес вводится только в граммах",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun HeaderCard() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RestaurantMenu,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }

            Column {
                Text(
                    text = "Калькулятор рецептов",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Быстрый расчёт ингредиентов для морковки и мяса",
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Composable
private fun MenuButton(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp),
        shape = RoundedCornerShape(24.dp),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 14.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecipeScreen(
    recipeType: RecipeType,
    onBack: () -> Unit
) {
    var weightInput by rememberSaveable { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }
    var resultLines by remember { mutableStateOf<List<String>>(emptyList()) }

    Scaffold(
        modifier = Modifier
            .safeDrawingPadding()
            .imePadding(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = recipeType.title,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = recipeType.accentTitle,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = recipeType.inputTitle,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = {
                            weightInput = it.replace(',', '.')
                            errorText = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Вес в граммах") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    if (errorText != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorText.orEmpty(),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = {
                                val weight = weightInput.toDoubleOrNull()
                                when {
                                    weight == null -> {
                                        errorText = "Введите корректный вес"
                                        resultLines = emptyList()
                                    }
                                    weight <= 0.0 -> {
                                        errorText = "Вес должен быть больше нуля"
                                        resultLines = emptyList()
                                    }
                                    else -> {
                                        errorText = null
                                        resultLines = calculateResults(recipeType, weight)
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text("Рассчитать")
                        }
                        OutlinedButton(
                            onClick = {
                                weightInput = ""
                                errorText = null
                                resultLines = emptyList()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text("Очистить")
                        }
                    }
                }
            }

            if (resultLines.isNotEmpty()) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Результат",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        resultLines.forEachIndexed { index, line ->
                            Text(
                                text = line,
                                fontSize = 18.sp
                            )
                            if (index != resultLines.lastIndex) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun calculateResults(recipeType: RecipeType, weightInGrams: Double): List<String> {
    val factor = weightInGrams / 1000.0
    return recipeType.ingredients.map { ingredient ->
        val amount = ingredient.amountPer1000g * factor
        "${ingredient.name} - ${formatNumber(amount)} ${ingredient.unitType.title}"
    }
}

private fun formatNumber(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format(Locale.US, "%.2f", value).trimEnd('0').trimEnd('.')
    }
}
