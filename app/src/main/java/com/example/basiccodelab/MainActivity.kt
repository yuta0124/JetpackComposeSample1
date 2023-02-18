package com.example.basiccodelab

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.compose.foundation.lazy.items
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.basiccodelab.ui.theme.BasicCodelabTheme
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicCodelabTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

//note: @Composableを付与することでコンポーズ可能な関数となる。

//note: 再利用可能なコンポーザブルのベストプラクティス
@Composable
private fun MyApp(modifier: Modifier = Modifier, names: List<String> = listOf("World", "Compose")) {
    //note: rememberSaveable → 状態が保存され、構成の変更（回転など）やプロセスの終了後も保持される。
    var shouldShowOnBoarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier) {
        if (shouldShowOnBoarding) {
            OnBoardingScreen(onContinueClicked = {
                shouldShowOnBoarding = false
            })
        } else {
            Greetings()
        }
    }
}

@Composable
fun Greetings(
    modifier: Modifier = Modifier,
    names: List<String> = List(1000) { "$it" }
) {
    LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
fun Greeting(name: String) {

    //note: 状態変更を再コンポジション前後で保持する
    var expanded by remember { mutableStateOf(false) }
    //note: アニメーションのカスタマイズ
    val extraPadding by animateDpAsState(
        if (expanded) 48.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding.coerceAtLeast(0.dp))
            ) {
                Text(text = "Hello, ")
                //note: 定義済みのスタイルを元に変更。
                Text(text = name, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold))
            }
            IconButton(onClick = {
                expanded = !expanded    //クリックをトリガーにexpanded変更
            }) {
                Icon(imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) stringResource(id = R.string.show_less) else stringResource(
                        id = R.string.show_more
                    ))
            }
        }
        //note: Composeの基本標準レイアウト要素のカラムを利用
//        Column(modifier = Modifier
//            .fillMaxWidth()
//            .padding(24.dp)) { //横幅を最大限利用し、padding設定
//            Text(text = "Hello,")
//            Text(text = name)
//        }
    }
}

@Composable
fun OnBoardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to the Basics Codelab!")
        Button(modifier = Modifier.padding(vertical = 24.dp), onClick = onContinueClicked) {
            Text("Continue")
        }
    }
}

//note :@Previewを付与することでAndroidプレビューが利用可能。widthDpでAndroid端末の最小の横幅である320を指定。
@Preview(showBackground = true, name = "Text Preview", widthDp = 320) //Androidプレビュー機能を有効にするため付与,パラメータにnameを追加することで複数作成可能
@Composable
fun DefaultPreview() {
    BasicCodelabTheme {
        MyApp()
    }
}

//note: ダークモードでプレビュー
@Preview(showBackground = true, widthDp = 320, uiMode = UI_MODE_NIGHT_YES, name = "Dark")
@Composable
private fun GreetingsPreview() {
    BasicCodelabTheme {
        Greetings()
    }
}

@Preview
@Composable
fun MyAppPreview() {
    BasicCodelabTheme {
        MyApp(Modifier.fillMaxSize())
    }
}