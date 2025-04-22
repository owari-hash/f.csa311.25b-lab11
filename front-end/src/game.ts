export interface GameState {
  cells: Cell[];
  status: string;
}

export interface Cell {
  x: number;
  y: number;
  text: string;
  playable: boolean;
}
