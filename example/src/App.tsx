import * as React from 'react';
import { StyleSheet, View, Button } from 'react-native';
import Fily from 'react-native-fily';

export default function App() {
  const open = async () => {
    const uri = await Fily.open();
    console.log(uri);
  };

  return (
    <View style={styles.container}>
      <Button title="OPEN" onPress={open} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
