package com.example.chronoheist

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow
import kotlin.math.sqrt
import org.apache.commons.lang3.tuple.MutablePair

class Level1Activity : AppCompatActivity() {

    private lateinit var hpText: TextView
    private lateinit var waveCount: TextView
    private lateinit var meatCount: TextView
    private lateinit var buyMenuFrame: FrameLayout

    private var healthPoints = 100
    private var waveNumber = 1

    private var startgold = 150
    private var gold = 0

    private var enemyCount = 10
    private var enemySpawnInterval = 2000 // 2 seconds between enemies
    private val waveInterval = 5000L // 10 seconds between waves

    private val enemies = mutableListOf<ImageView>()
    private val towers = mutableListOf<ImageView>()

    private var towerCooldown = false

    private var isGamePaused = false
    private var savedGameState: Bundle? = null

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var enemyRunnable: Runnable
    private lateinit var waveRunnable: Runnable

    companion object {
        private const val KEY_SAVED_STATE = "saved_state"
        private const val KEY_HEALTH_POINTS = "health_points"
        private const val KEY_WAVE_NUMBER = "wave_number"
        private const val KEY_GOLD = "gold"
        // Define other keys as needed
    }

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_level)

        hpText = findViewById(R.id.hptext)
        waveCount = findViewById(R.id.waveText)
        meatCount = findViewById(R.id.meatText)

        buyMenuFrame = findViewById(R.id.buyMenuFrame)
        buyMenuFrame.visibility = View.GONE

        gold = startgold
        meatCount.text = gold.toString()

        updateUI()

        setupGame()
        setupTowerPlacement()

        if (savedInstanceState != null) {
            savedGameState = savedInstanceState.getBundle(KEY_SAVED_STATE)
            if (savedGameState != null) {
                restoreGameState(savedGameState!!)
                if (isGamePaused) {
                    showPauseMenu()
                }
            }
        }

        // Assuming you have references to your buttons
        val pauseButton = findViewById<ImageButton>(R.id.pause_bttn)
        val quitButton = findViewById<ImageButton>(R.id.pauseOptionsBttn2)
        val closeButton = findViewById<ImageButton>(R.id.CloseBttn)

        // Set click listeners for the buttons
        pauseButton.setOnClickListener {
            showPauseMenu()
        }

        quitButton.setOnClickListener {
            quitGame()
        }

        closeButton.setOnClickListener {
            hidePauseMenu()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save game state if the game is paused
        if (isGamePaused) {
            savedGameState = Bundle()
            saveGameState(savedGameState!!)
            outState.putBundle(KEY_SAVED_STATE, savedGameState)
        }
    }

    private fun saveGameState(outState: Bundle) {
        // Save relevant game state variables
        outState.putInt(KEY_HEALTH_POINTS, healthPoints)
        outState.putInt(KEY_WAVE_NUMBER, waveNumber)
        outState.putInt(KEY_GOLD, gold)
        // Add other relevant variables here
    }

    private fun restoreGameState(savedState: Bundle) {
        // Restore game state from saved bundle
        healthPoints = savedState.getInt(KEY_HEALTH_POINTS)
        waveNumber = savedState.getInt(KEY_WAVE_NUMBER)
        gold = savedState.getInt(KEY_GOLD)
        // Update UI with restored values
        updateUI()
    }

    private fun showPauseMenu() {
        // Show the pause menu
        findViewById<View>(R.id.PauseMenu).visibility = View.VISIBLE
        // Pause the game logic (stop enemy spawning, tower cooldown, etc.)
        pauseGameLogic()
    }

    private fun hidePauseMenu() {
        // Hide the pause menu
        findViewById<View>(R.id.PauseMenu).visibility = View.GONE
        // Resume the game logic (start enemy spawning, tower cooldown, etc.)
        resumeGameLogic()
    }

    private fun pauseGameLogic() {
        // Pause game logic here
        handler.removeCallbacks(enemyRunnable)
        handler.removeCallbacks(waveRunnable)
        handler.removeCallbacksAndMessages(null) // Remove all callbacks, including gold income
        isGamePaused = true

        // Pause enemy animations and movement
        for (enemy in enemies) {
            enemy.clearAnimation() // Stop the animation
            enemy.animate().cancel() // Cancel the movement animation
        }

        // Start enemy spawning logic if the game is not over
        if (healthPoints > 0) {
            startEnemySpawning()
        }
    }

    private fun resumeGameLogic() {
        // Resume game logic here
        if (isGamePaused) {
            isGamePaused = false
            handler.post(enemyRunnable)
            handler.post(waveRunnable)

            // Resume enemy animations and movement
            for (enemy in enemies) {
                val animationDrawable = enemy.background as? AnimationDrawable
                animationDrawable?.start() // Start the animation
                moveEnemy(enemy, enemy.y) // Resume the movement animation
            }

            // Restart tower cooldown mechanism
            startArcherTowerCooldown()
            startKnightTowerCooldown()
        }
    }

    private fun quitGame() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun setupGame() {

        // Initialize towers
        initializeTowers()

        // Initialize waveRunnable
        waveRunnable = Runnable {
            // Start the wave logic
            startWave()
        }

        // Start the first wave
        startWave()

        // Start enemy spawning logic
        startEnemySpawning()
    }

    private fun startEnemySpawning() {
        // Start enemy spawning for the current wave
        handler.post(enemyRunnable)
    }

    private fun initializeTowers() {
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
    }

    @SuppressLint("ClickableViewAccessibility")
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

    @SuppressLint("SetTextI18n")
    private fun startWave() {
        waveCount.text = "Wave $waveNumber"

        // Start enemy spawning for the current wave
        var enemiesSpawned = 0
        var isSpawning = false // Flag to track whether enemy spawning is in progress

        enemyRunnable = Runnable {
            if (!isSpawning && enemiesSpawned < enemyCount) {
                isSpawning = true // Set flag to indicate that spawning is in progress
                spawnEnemy()
                enemiesSpawned += 1

                // Schedule the next enemy spawn if there are more enemies to spawn
                if (enemiesSpawned < enemyCount) {
                    // Schedule the next enemy spawn after the enemySpawnInterval duration
                    handler.postDelayed({
                        isSpawning = false // Reset the flag before scheduling the next spawn
                        handler.post(enemyRunnable) // Schedule the next enemy spawn
                    }, enemySpawnInterval.toLong())
                }
            }
        }

        // Start the first enemy spawn
        handler.post(enemyRunnable)
    }


    private fun startNextWave() {
        enemyCount = (enemyCount * 1.2).toInt() // Increase enemy count proportionally

        // Check if there are no more enemies remaining
        if (enemies.isEmpty()) {
            // Delay before starting the next wave
            handler.postDelayed({
                waveNumber += 1
                enemySpawnInterval = (enemySpawnInterval * 0.95).toInt()
                handler.postDelayed({ startNextWave() }, waveInterval)
                startWave()
            }, waveInterval)
        } else {
            // If enemies are still present, wait and check again after a short delay
            handler.postDelayed({
                startNextWave()
            }, 1000) // Check again after 1 second
        }
    }

    private fun spawnEnemy() {
        // Check if the game is paused
        if (isGamePaused) return

        // Determine paths available based on the wave number
        val paths = if (waveNumber >= 10) {
            listOf(R.id.path1, R.id.path2, R.id.path3)
        } else if (waveNumber >= 5) {
            listOf(R.id.path1, R.id.path2)
        } else {
            listOf(R.id.path2)
        }

        // Randomly select one of the available paths
        val pathId = paths.random()
        val path = findViewById<LinearLayout>(pathId)
        val pathCenterY = path.y + path.height / 2f

        val enemyInitialPosition = MutablePair(-50f, pathCenterY - 125) // Store initial position

        val enemy = ImageView(this).apply {
            setBackgroundResource(R.drawable.enemy_run_animation)
            layoutParams = RelativeLayout.LayoutParams(250, 250)
            x = enemyInitialPosition.left // Set x coordinate
            y = enemyInitialPosition.right // Set y coordinate
        }

        findViewById<RelativeLayout>(R.id.main).addView(enemy)
        enemies.add(enemy)

        // Start the animation
        val animationDrawable = enemy.background as AnimationDrawable
        animationDrawable.start()

        // Store the initial position in the enemy's tag
        enemy.tag = enemyInitialPosition

        // Move enemy if the game is not paused
        if (!isGamePaused) {
            moveEnemy(enemy, pathCenterY - 125)
        } else {
            // Stop the movement animation if the game is paused
            enemy.clearAnimation()
        }
    }

    private fun moveEnemy(enemy: ImageView, pathCenterY: Float) {
        enemy.animate()
            .x(1450f)
            .y(pathCenterY)
            .setDuration(10000)
            .withEndAction {
                // Update enemy's position as it moves
                val initialPosition = enemy.tag as MutablePair<Float, Float>
                initialPosition.left = enemy.x
                initialPosition.right = enemy.y

                // Check if the enemy still exists in the enemies list
                if (enemies.contains(enemy)) {
                    // Reduce health only if the enemy has not been killed yet
                    healthPoints -= 1
                    updateUI()
                }

                // Remove enemy from view
                findViewById<RelativeLayout>(R.id.main).removeView(enemy)
                enemies.remove(enemy)

                if (enemies.isEmpty()) {
                    // If there are no more enemies, start the next wave after waveInterval
                    handler.postDelayed({ startNextWave() }, waveInterval)
                } else {
                    // If there are still enemies, do nothing
                }

                if (healthPoints <= 0) {
                    gameOver()
                }
            }
    }



    private val occupiedTowerTiles = mutableSetOf<Int>()

    private fun placeTower(x: Float, y: Float) {
        // Loop through all tower placement areas
        for (towerPlacementArea in towers) {
            val location = IntArray(2)
            towerPlacementArea.getLocationOnScreen(location)
            val towerX = location[0]
            val towerY = location[1]
            val towerWidth = towerPlacementArea.width
            val towerHeight = towerPlacementArea.height

            if (x >= towerX && x < towerX + towerWidth && y >= towerY && y < towerY + towerHeight) {
                // Check if the tower placement area is already occupied
                val towerId = towerPlacementArea.id
                if (occupiedTowerTiles.contains(towerId)) {
                    // Tower placement area is already occupied, return without placing the tower
                    return
                }

                // Show the buy menu frame
                buyMenuFrame.visibility = View.VISIBLE

                // Set up the confirm and cancel buttons in the buy menu frame
                val archerTowerButton = buyMenuFrame.findViewById<Button>(R.id.archerTowerBttn)
                val goldMineButton = buyMenuFrame.findViewById<Button>(R.id.goldMineBttn)
                val barracksButton = buyMenuFrame.findViewById<Button>(R.id.barracksBttn)
                val cancelButton = buyMenuFrame.findViewById<ImageButton>(R.id.imageButton3)

                barracksButton.setOnClickListener(){
                    if (gold >= 200) {
                        gold -= 200
                        updateUI()

                        // Place tower at the center of the tower placement area
                        val tower = ImageView(this).apply {
                            setImageResource(R.drawable.house_blue) // Replace with your tower image resource
                            layoutParams = RelativeLayout.LayoutParams(250, 250).apply {
                                // Center the tower within the tower placement area
                                this.leftMargin = towerX + (towerWidth - 250) / 2
                                this.topMargin = towerY - towerHeight
                            }


                        }
                        findViewById<RelativeLayout>(R.id.main).addView(tower)

                        // Add the tower ID to the set of occupied tower placement areas
                        occupiedTowerTiles.add(towerId)

                        // Make the tower placement area invisible
                        towerPlacementArea.visibility = View.GONE

                        // Place knight at the door of barracks
                        val knight = ImageView(this).apply {
                            id = R.id.knight_blue // Set the id
                            setBackgroundResource(R.drawable.knight_attack_animation) // Set the animation for the archer
                            layoutParams = RelativeLayout.LayoutParams(150, 150).apply {
                                // Center the archer above the tower
                                this.leftMargin = (towerX + (towerWidth - 150) / 2) - 30
                                this.topMargin = towerY + (towerHeight/2) // Adjust as needed to position the archer above the tower
                            }
                        }
                        findViewById<RelativeLayout>(R.id.main).addView(knight)

                        // Start the animation
                        val archerAnimationDrawable = knight.background as AnimationDrawable
                        archerAnimationDrawable.start()

                        // Make the archer visible
                        knight.visibility = View.VISIBLE

                        // Place knight at the door of barracks
                        val knight2 = ImageView(this).apply {
                            id = R.id.knight_blue // Set the id
                            setBackgroundResource(R.drawable.knight_attack_animation) // Set the animation for the archer
                            layoutParams = RelativeLayout.LayoutParams(150, 150).apply {
                                // Center the archer above the tower
                                this.leftMargin = (towerX + (towerWidth - 150) / 2) + 30
                                this.topMargin = towerY + (towerHeight/2) // Adjust as needed to position the archer above the tower
                            }
                        }
                        findViewById<RelativeLayout>(R.id.main).addView(knight2)

                        // Start the animation
                        val knight2AnimationDrawable = knight2.background as AnimationDrawable
                        knight2AnimationDrawable.start()

                        // Make the archer visible
                        knight2.visibility = View.VISIBLE

                        // Hide the buy menu frame
                        buyMenuFrame.visibility = View.GONE

                        // Start tower cooldown runnable
                        startKnightTowerCooldown()
                    }
                }

                goldMineButton.setOnClickListener(){
                    if (gold >= 200) {
                        gold -= 200
                        updateUI()

                        // Place tower at the center of the tower placement area
                        val tower = ImageView(this).apply {
                            setImageResource(R.drawable.goldmine_active) // Replace with your tower image resource
                            layoutParams = RelativeLayout.LayoutParams(250, 250).apply {
                                // Center the tower within the tower placement area
                                this.leftMargin = towerX + (towerWidth - 250) / 2
                                this.topMargin = towerY - towerHeight + 75
                            }
                        }
                        findViewById<RelativeLayout>(R.id.main).addView(tower)

                        // Add the tower ID to the set of occupied tower placement areas
                        occupiedTowerTiles.add(towerId)

                        // Make the tower placement area invisible
                        towerPlacementArea.visibility = View.GONE

                        // Hide the buy menu frame
                        buyMenuFrame.visibility = View.GONE

                        startGoldmineIncome()
                    }
                }

                archerTowerButton.setOnClickListener {
                    // Confirm the purchase
                    if (gold >= 100) {
                        gold -= 100
                        updateUI()

                        // Place tower at the center of the tower placement area
                        val tower = ImageView(this).apply {
                            setImageResource(R.drawable.tower_blue) // Replace with your tower image resource
                            layoutParams = RelativeLayout.LayoutParams(250, 250).apply {
                                // Center the tower within the tower placement area
                                this.leftMargin = towerX + (towerWidth - 250) / 2
                                this.topMargin = towerY - towerHeight
                            }
                        }
                        findViewById<RelativeLayout>(R.id.main).addView(tower)

                        // Add the tower ID to the set of occupied tower placement areas
                        occupiedTowerTiles.add(towerId)

                        // Make the tower placement area invisible
                        towerPlacementArea.visibility = View.GONE

                        // Place archer at the top of the tower
                        val archer = ImageView(this).apply {
                            id = R.id.archer_blue // Set the id
                            setBackgroundResource(R.drawable.archer_idle_animation) // Set the animation for the archer
                            layoutParams = RelativeLayout.LayoutParams(150, 150).apply {
                                // Center the archer above the tower
                                this.leftMargin = towerX + (towerWidth - 150) / 2
                                this.topMargin = towerY - towerHeight // Adjust as needed to position the archer above the tower
                            }
                        }
                        findViewById<RelativeLayout>(R.id.main).addView(archer)

                        // Start the animation
                        val archerAnimationDrawable = archer.background as AnimationDrawable
                        archerAnimationDrawable.start()

                        // Make the archer visible
                        archer.visibility = View.VISIBLE

                        // Hide the buy menu frame
                        buyMenuFrame.visibility = View.GONE

                        // Start tower cooldown runnable
                        startArcherTowerCooldown()
                    }
                }

                cancelButton.setOnClickListener {
                    // Cancel the purchase and hide the buy menu frame
                    buyMenuFrame.visibility = View.GONE
                }

                // Exit the function after showing the buy menu frame
                return
            }
        }
    }

    private fun startKnightTowerCooldown() {
        handler.post(object : Runnable {
            override fun run() {
                for (tower in towers) {
                    checkForEnemiesInRangeKnight(tower, handler)
                }
                handler.postDelayed(this, 1000) // Check for enemies every second
            }
        })
    }

    private fun startArcherTowerCooldown() {
        handler.post(object : Runnable {
            override fun run() {
                for (tower in towers) {
                    checkForEnemiesInRange(tower, handler)
                }
                handler.postDelayed(this, 1000) // Check for enemies every second
            }
        })
    }

    private fun checkForEnemiesInRange(tower: ImageView, handler: Handler) {
        val towerX = tower.x
        val towerY = tower.y
        val towerWidth = tower.width
        val towerHeight = tower.height

        // If the game is paused, or tower cooldown is active, exit the function
        if (isGamePaused || towerCooldown) return

        // Iterate through enemies to check if any are within tower range
        val iterator = enemies.iterator()
        while (iterator.hasNext()) {
            val enemy = iterator.next()
            val enemyX = enemy.x
            val enemyY = enemy.y

            // Calculate distance between tower and enemy
            val distance = sqrt(
                (enemyX - towerX).toDouble().pow(2.0) +
                        (enemyY - towerY).toDouble().pow(2.0)
            )

            // If enemy is within range, attack it and set cooldown
            if (distance <= ((towerWidth + towerHeight))/2) {
                // Attack enemy (implement your attack logic here)
                // For example:
                // enemy healthPoints -= tower damage
                // if enemy healthPoints <= 0, remove enemy from view and enemies list
                gold += 2
                updateUI()
                iterator.remove()
                findViewById<RelativeLayout>(R.id.main).removeView(enemy)

                // Set tower cooldown
                towerCooldown = true
                handler.postDelayed({ towerCooldown = false }, 2000) // 2-second cooldown
                println("TowerAttack Archer Tower attacked an enemy")
            }
        }
    }

    private fun checkForEnemiesInRangeKnight(tower: ImageView, handler: Handler) {
        val towerX = tower.x
        val towerY = tower.y
        val towerWidth = tower.width
        val towerHeight = tower.height

        // If the game is paused, or tower cooldown is active, exit the function
        if (isGamePaused || towerCooldown) return

        // Iterate through enemies to check if any are within tower range
        val iterator = enemies.iterator()
        while (iterator.hasNext()) {
            val enemy = iterator.next()
            val enemyX = enemy.x
            val enemyY = enemy.y

            // Calculate distance between tower and enemy
            val distance = sqrt(
                (enemyX - towerX).toDouble().pow(2.0) +
                        (enemyY - towerY).toDouble().pow(2.0)
            )

            // If enemy is within range, attack it and set cooldown
            if (distance <= ((towerWidth + towerHeight))/2) {
                // Attack enemy (implement your attack logic here)
                // For example:
                // enemy healthPoints -= tower damage
                // if enemy healthPoints <= 0, remove enemy from view and enemies list
                gold += 2
                updateUI()
                iterator.remove()
                findViewById<RelativeLayout>(R.id.main).removeView(enemy)

                // Set tower cooldown
                towerCooldown = true
                handler.postDelayed({ towerCooldown = false }, 2000) // 2-second cooldown
                println("TowerAttack Knight Tower attacked an enemy")
            }
        }
    }

    private fun startGoldmineIncome() {
        handler.post(object : Runnable {
            override fun run() {
                if (!isGamePaused) {
                    gold += 25
                    updateUI()
                }
                handler.postDelayed(this, 3000) // 3 seconds interval
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        hpText.text = "HP: $healthPoints"
        waveCount.text = "Wave $waveNumber"
        meatCount.text = gold.toString()
    }

    private fun gameOver() {
        // Stop the game and show a game over message
        handler.removeCallbacks(enemyRunnable)
        handler.removeCallbacks(waveRunnable)

        // Start LevelSelectActivity
        val intent = Intent(this, LevelSelectActivity::class.java)
        startActivity(intent)

        // Finish the current activity
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(enemyRunnable)
        handler.removeCallbacks(waveRunnable)
        handler.removeCallbacksAndMessages(null) // Remove all callbacks, including gold income
    }


}
