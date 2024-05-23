package com.example.chronoheist

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class Level1Activity : AppCompatActivity() {

    private lateinit var hpText: TextView
    private lateinit var waveCount: TextView
    private lateinit var meatCount: TextView

    private var healthPoints = 100
    private var waveNumber = 1
    private val startermeat = 150

    private var meat = 0

    private val handler = Handler(Looper.getMainLooper())
    private val towerHandler = Handler(Looper.getMainLooper())
    private val waveHandler = Handler(Looper.getMainLooper())

    private var enemyCount = 10
    private val enemySpawnInterval = 2000L // 2 seconds between enemies
    private val waveInterval = 10000L // 5 seconds between waves

    private val enemies = mutableListOf<ImageView>()
    private val towers = mutableListOf<ImageView>()

    private lateinit var enemyRunnable: Runnable
    private lateinit var waveRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        hpText = findViewById(R.id.hptext)
        waveCount = findViewById(R.id.waveText)
        meatCount = findViewById(R.id.meatText)

        updateUI()

        setupGame()
        setupTowerPlacement()
    }

    private fun setupGame() {
        meat = startermeat
        meatCount.text = startermeat.toString()
        // Initialize towers
        initializeTowers()

        // Start the first wave
        startWave()
    }

    private fun initializeTowers() {
        // Example: Add towers to the list
        towers.add(findViewById(R.id.path1tower1))
        towers.add(findViewById(R.id.path1tower2))
        towers.add(findViewById(R.id.path1tower3))
        towers.add(findViewById(R.id.path1tower4))
        towers.add(findViewById(R.id.path2tower1))
        towers.add(findViewById(R.id.path2tower2))
        towers.add(findViewById(R.id.path2tower3))
        towers.add(findViewById(R.id.path2tower4))
        towers.add(findViewById(R.id.path3tower1))
        towers.add(findViewById(R.id.path3tower2))
        towers.add(findViewById(R.id.path3tower3))
        towers.add(findViewById(R.id.path3tower4))
        towers.add(findViewById(R.id.path4tower1))
        towers.add(findViewById(R.id.path4tower2))
        towers.add(findViewById(R.id.path4tower3))
        towers.add(findViewById(R.id.path4tower4))
        // ...add other towers as needed
    }

    private fun setupTowerPlacement() {
        // Detect touch events for tower placement
        findViewById<RelativeLayout>(R.id.main).setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                placeTower(event.x, event.y)
                return@setOnTouchListener true
            }
            false
        }
    }

    private fun startWave() {

        waveNumber++
        enemyCount = (enemyCount * 1.2).toInt() // Increase enemy count proportionally
        waveCount.text = "Wave $waveNumber"
        updateUI()

        // Start enemy spawning for the current wave
        var enemiesSpawned = 0
        enemyRunnable = object : Runnable {
            override fun run() {
                if (enemiesSpawned < enemyCount) {
                    spawnEnemy()
                    enemiesSpawned++
                    waveHandler.postDelayed(this, enemySpawnInterval)
                }
            }
        }
        waveHandler.post(enemyRunnable)
    }

    private fun spawnEnemy() {
        // Determine paths available based on the wave number
        val paths = when {
            waveNumber >= 10 -> listOf(R.id.path1, R.id.path2, R.id.path3)
            waveNumber >= 5 -> listOf(R.id.path1, R.id.path2)
            else -> listOf(R.id.path2)
        }

        // Randomly select one of the available paths
        val pathId = paths[Random.nextInt(paths.size)]
        val path = findViewById<LinearLayout>(pathId)
        val pathCenterY = path.y + path.height / 2f

        val enemy = ImageView(this).apply {
            setImageResource(R.drawable.enemy_run_animation)
            layoutParams = RelativeLayout.LayoutParams(250, 250)
            x = -200f
            y = pathCenterY - 125
        }

        findViewById<RelativeLayout>(R.id.main).addView(enemy)
        enemies.add(enemy)

        // Start the animation
        val animationDrawable = enemy.drawable as AnimationDrawable
        animationDrawable.start()

        moveEnemy(enemy, pathCenterY - 125)
    }


    private fun moveEnemy(enemy: ImageView, pathCenterY: Float) {
        enemy.animate()
            .x(1450f)
            .y(pathCenterY)
            .setDuration(10000)
            .withEndAction {
                // Reduce health when an enemy reaches the end
                healthPoints -= 1
                updateUI()

                // Remove enemy from view
                findViewById<RelativeLayout>(R.id.main).removeView(enemy)
                enemies.remove(enemy)

                if (enemies.isEmpty()) {
                    // If there are no more enemies, start the next wave after waveInterval
                    waveHandler.postDelayed({ startWave() }, waveInterval)
                } else {
                    // If there are still enemies, do nothing
                }

                if (healthPoints <= 0) {
                    gameOver()
                }
            }
    }


    private fun placeTower(x: Float, y: Float) {
        if (meatCount.text.toString().toInt() >= 100) {
            meat -= 100
            updateUI()
            // Loop through all tower placement areas
            for (towerPlacementArea in towers) {
                val location = IntArray(2)
                towerPlacementArea.getLocationOnScreen(location)
                val towerX = location[0]
                val towerY = location[1]
                val towerWidth = towerPlacementArea.width
                val towerHeight = towerPlacementArea.height

                if (x >= towerX && x < towerX + towerWidth && y >= towerY && y < towerY + towerHeight) {
                    // Place tower at the center of the tower placement area
                    val tower = ImageView(this).apply {
                        setImageResource(R.drawable.tower_blue) // Replace with your tower image resource
                        layoutParams = RelativeLayout.LayoutParams(250, 250) // Adjust size as needed
                        this.x = (towerX - towerWidth / 2).toFloat()
                        this.y = (towerY - towerHeight).toFloat()
                    }
                    findViewById<RelativeLayout>(R.id.main).addView(tower)
                    towers.add(tower)

                    // Place archer at the top of the tower
                    val archer = ImageView(this).apply {
                        id = R.id.archer_blue // Set the id
                        // Set the animation for the archer
                        setBackgroundResource(R.drawable.archer_idle_animation)
                        layoutParams = RelativeLayout.LayoutParams(150, 150).apply {
                            // Set position on top of the tower
                            topMargin = (tower.y).toInt()
                            leftMargin = (tower.x + towerWidth / 2 - 10).toInt()
                        }
                    }
                    findViewById<RelativeLayout>(R.id.main).addView(archer)

                    // Start the animation for the archer
                    val archerAnimationDrawable = archer.background as AnimationDrawable
                    archerAnimationDrawable.start()

                    // Make the archer visible
                    archer.visibility = View.VISIBLE

                    // Start the recurring task to check for enemies
                    startTowerAttackTask(tower)

                    return  // Exit the function after placing the tower
                }
            }
        }
    }

    private fun startTowerAttackTask(tower: ImageView) {
        val towerAttackTask = object : Runnable {
            override fun run() {
                // Check for enemies within range
                val closestEnemy = findClosestEnemyWithinRange(tower)
                closestEnemy?.let {
                    // Kill the closest enemy
                    findViewById<RelativeLayout>(R.id.main).removeView(it)
                    enemies.remove(it)

                    // Increment meat count
                    meat += 10
                    updateUI()
                }

                // Schedule the next check in 2 seconds
                towerHandler.postDelayed(this, 2000L)
            }
        }
        // Start the task immediately
        towerHandler.post(towerAttackTask)
    }

    private fun findClosestEnemyWithinRange(tower: ImageView): ImageView? {
        val towerCenterX = tower.x + tower.width / 2
        val towerCenterY = tower.y + tower.height / 2
        val range = 75 // Tower's attack range

        return enemies.minByOrNull { enemy ->
            val enemyCenterX = enemy.x + enemy.width / 2
            val enemyCenterY = enemy.y + enemy.height / 2
            val distance = Math.sqrt(
                ((enemyCenterX - towerCenterX) * (enemyCenterX - towerCenterX) +
                        (enemyCenterY - towerCenterY) * (enemyCenterY - towerCenterY)).toDouble()
            )
            if (distance <= range) distance else Double.MAX_VALUE
        }
    }


    private fun updateUI() {
        hpText.text = "HP: $healthPoints"
        waveCount.text = "Wave $waveNumber"
        meatCount.text = meat.toString()
    }

    private fun gameOver() {
        // Stop the game and show a game over message
        handler.removeCallbacks(enemyRunnable)
        handler.removeCallbacks(waveRunnable)
        // Show game over UI (not implemented here)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(enemyRunnable)
        handler.removeCallbacks(waveRunnable)
    }
}
