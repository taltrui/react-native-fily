import { NativeModules } from 'react-native';

type FilyType = {
  multiply(a: number, b: number): Promise<number>;
  add(a: number, b: number): Promise<number>;
  open(): Promise<void>;
};

const { Fily } = NativeModules;

export default Fily as FilyType;
