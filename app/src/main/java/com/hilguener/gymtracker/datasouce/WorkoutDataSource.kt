package com.hilguener.gymtracker.datasouce

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hilguener.gymtracker.model.Exercise
import com.hilguener.gymtracker.model.Workout
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow

class WorkoutDataSource(userId: String) {
    private val database = FirebaseDatabase.getInstance()
    private val workoutsRef = database.reference.child("users").child(userId).child("workouts")
    private val _allWorkouts = MutableStateFlow<MutableList<Workout>>(mutableListOf())
    val allWorkouts: StateFlow<MutableList<Workout>> = _allWorkouts

    fun saveWorkout(
        name: String,
        description: String,
        date: String,
        time: String,
    ) {
        val workoutId = workoutsRef.push().key
        if (workoutId != null) {
            val workoutData = Workout(workoutId, name, description, date, time, emptyList())
            workoutsRef.child(workoutId).setValue(workoutData).addOnSuccessListener {
                println("Workout added with ID: $workoutId")
            }.addOnFailureListener { e ->
                println("Error adding workout: $e")
            }
        }
    }

    fun fetchWorkouts(): Flow<MutableList<Workout>> {
        workoutsRef.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val workoutList: MutableList<Workout> = mutableListOf()
                    for (workoutSnapshot in dataSnapshot.children) {
                        val workOutId =
                            workoutSnapshot.child("workoutId").getValue(String::class.java)
                                .toString()
                        val name =
                            workoutSnapshot.child("workoutName").getValue(String::class.java)
                                .toString()
                        val description =
                            workoutSnapshot.child("workoutDescription").getValue(String::class.java)
                                .toString()
                        val date =
                            workoutSnapshot.child("date").getValue(String::class.java).toString()
                        val time =
                            workoutSnapshot.child("time").getValue(String::class.java).toString()

                        val exercisesSnapshot = workoutSnapshot.child("exercises")
                        val exerciseList: MutableList<Exercise> = mutableListOf()
                        if (exercisesSnapshot.exists()) {
                            for (exerciseSnapshot in exercisesSnapshot.children) {
                                val exercise = Exercise("", "", "", 0, 0)
                                exerciseList.add(exercise)
                            }
                        }

                        val workout =
                            Workout(workOutId, name, description, date, time, exerciseList)
                        workoutList.add(workout)
                    }
                    _allWorkouts.value = workoutList
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("Error fetching workouts: ${databaseError.message}")
                }
            },
        )
        return allWorkouts
    }

    fun deleteWorkout(workoutId: String) {
        workoutsRef.child(workoutId).removeValue().addOnSuccessListener {
            println("Workout deleted with ID: $workoutId")
        }.addOnFailureListener { e ->
            println("Error deleting workout: $e")
        }
    }

    fun deleteExerciseInWorkout(
        workoutId: String,
        exerciseId: String,
    ) {
        val exerciseRef =
            workoutsRef
                .child(workoutId)
                .child("exercises")
                .child(exerciseId)

        exerciseRef.removeValue().addOnSuccessListener {
            println("Exercise deleted with ID: $exerciseId from workout: $workoutId")
        }.addOnFailureListener { e ->
            println("Error deleting exercise: $e")
        }
    }

    fun updateWorkout(
        workoutId: String,
        name: String,
    ) {
        val updates =
            hashMapOf<String, Any>(
                "workoutName" to name,
            )
        workoutsRef.child(workoutId).updateChildren(updates).addOnSuccessListener {
            println("Workout updated with ID: $workoutId")
        }.addOnFailureListener { e ->
            println("Error updating workout: $e")
        }
    }

    fun updateExercise(
        workoutId: String,
        exerciseId: String,
        exerciseName: String,
        observations: String,
    ) {
        val exerciseRef =
            workoutsRef
                .child(workoutId)
                .child("exercises")
                .child(exerciseId)

        val updates =
            hashMapOf<String, Any>(
                "exerciseName" to exerciseName,
                "observations" to observations,
            )

        exerciseRef.updateChildren(updates).addOnSuccessListener {
            println("Exercise updated with ID: $exerciseId in workout: $workoutId")
        }.addOnFailureListener { e ->
            println("Error updating exercise: $e")
        }
    }

    fun saveExercise(
        workoutId: String,
        exerciseName: String,
        observations: String,
    ) {
        val exerciseData =
            hashMapOf(
                "exerciseName" to exerciseName,
                "observations" to observations,
            )

        val newExerciseRef =
            workoutsRef
                .child(workoutId)
                .child("exercises")
                .push()

        val newExerciseId = newExerciseRef.key

        newExerciseId?.let {
            newExerciseRef.setValue(exerciseData)
                .addOnSuccessListener {
                    println("Exercise added to workout: $workoutId with ID: $newExerciseId")
                }.addOnFailureListener { e ->
                    println("Error adding exercise: $e")
                }
        }
    }

    fun fetchExercises(workoutId: String): Flow<MutableList<Exercise>> {
        return callbackFlow {
            val eventListener =
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val exerciseList: MutableList<Exercise> = mutableListOf()
                        for (exerciseSnapshot in dataSnapshot.children) {
                            val exerciseId = exerciseSnapshot.key ?: ""
                            val exerciseName = exerciseSnapshot.child("exerciseName").getValue(String::class.java) ?: ""
                            val observations = exerciseSnapshot.child("observations").getValue(String::class.java) ?: ""
                            val sets = exerciseSnapshot.child("sets").getValue(Int::class.java) ?: 0
                            val rest = exerciseSnapshot.child("rest").getValue(Int::class.java) ?: 0

                            val exercise = Exercise(exerciseId, exerciseName, observations, sets, rest)
                            exerciseList.add(exercise)
                        }

                        trySend(exerciseList)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        close(databaseError.toException())
                    }
                }

            workoutsRef.child(workoutId).child("exercises").addValueEventListener(eventListener)

            awaitClose {
                workoutsRef.child(workoutId).child("exercises").removeEventListener(eventListener)
            }
        }
    }
}
