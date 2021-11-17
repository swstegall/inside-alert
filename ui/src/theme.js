import { createTheme } from "@mui/material/styles";

const theme = createTheme({
  palette: {
    primary: {
      main: "#4caf50",
      light: "#80e27e",
      dark: "#087f23",
      contrastText: "#fff",
    },
    secondary: {
      main: "#ff3d00",
      light: "#ff8a50",
      dark: "#c41c00",
    },
    error: {
      main: "#ef5350",
      light: "#ff867c",
      dark: "#b61827",
    },
    background: {
      default: "#dbdbdb",
    },
  },
});

export default theme;
