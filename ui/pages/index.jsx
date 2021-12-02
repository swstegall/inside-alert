import * as React from "react";
import Head from "next/head";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import KeyIcon from "@mui/icons-material/Key";
import KeyOffIcon from "@mui/icons-material/KeyOff";
import BarChartIcon from "@mui/icons-material/BarChart";
import InsightsIcon from "@mui/icons-material/Insights";
import { CircularProgress } from "@mui/material";
import axios from "axios";
import PageCard from "../src/individualComponents/PageCard";
import constants from "../src/utilities/constants";
import Stock from "../src/individualComponents/Stock";

const Index = () => {
  const [apiKeyID, setAPIKeyID] = React.useState("");
  const [secretKey, setSecretKey] = React.useState("");
  const [keysSet, setKeysSet] = React.useState(false);
  const [valuesSelected, setValuesSelected] = React.useState(false);
  const [scoresSelected, setScoresSelected] = React.useState(false);
  const [valuesLoaded, setValuesLoaded] = React.useState(false);
  const [scoresLoaded, setScoresLoaded] = React.useState(false);
  const [values, setValues] = React.useState(null);
  const [scores, setScores] = React.useState(null);

  const changeKeys = () => {
    setValuesSelected(false);
    setScoresSelected(false);
    setValuesLoaded(false);
    setScoresLoaded(false);
    setValues(null);
    setScores(null);
    setAPIKeyID("");
    setSecretKey("");
    setKeysSet(false);
  };

  const loadValues = async () => {
    setValuesSelected(true);
    setScoresSelected(false);
    setValuesLoaded(false);
    setScoresLoaded(false);
    setValues(null);
    setScores(null);
    try {
      const response = await axios({
        method: "get",
        url: `${constants.APIUrl}/values`,
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          "APCA-API-KEY-ID": apiKeyID,
          "APCA-API-SECRET-KEY": secretKey,
        },
      });
      setValues(response.data);
      setValuesLoaded(true);
    } catch (error) {
      console.log("Error!");
    }
  };

  const loadScores = async () => {
    setScoresSelected(true);
    setValuesSelected(false);
    setValuesLoaded(false);
    setScoresLoaded(false);
    setValues(null);
    setScores(null);
  };

  return (
    <>
      <Head>
        <title>Inside Alert</title>
      </Head>
      <AppBar position="static">
        <Toolbar variant="dense">
          <Typography variant="h6" color="inherit" component="div">
            Inside Alert
          </Typography>
        </Toolbar>
      </AppBar>
      <div className={"container"}>
        <PageCard
          render={
            <>
              <div className={"row"}>
                <div className={"col"}>
                  <TextField
                    fullWidth
                    id="api-key-id"
                    label="API Key ID"
                    value={apiKeyID}
                    onChange={(e) => setAPIKeyID(e.target.value)}
                    disabled={keysSet}
                  />
                </div>
                <div className={"col"}>
                  <TextField
                    fullWidth
                    id="secret-key"
                    label="Secret Key"
                    value={secretKey}
                    onChange={(e) => setSecretKey(e.target.value)}
                    disabled={keysSet}
                  />
                </div>
              </div>
              <div className={"row pt-3"}>
                <div
                  className={
                    "col d-flex align-items-center justify-content-flex-begin"
                  }
                >
                  <div className={"pr-2"}>
                    {keysSet ? (
                      <Button
                        variant="contained"
                        onClick={changeKeys}
                        startIcon={<KeyOffIcon />}
                      >
                        Change Keys
                      </Button>
                    ) : (
                      <Button
                        variant="contained"
                        onClick={() => setKeysSet(true)}
                        disabled={apiKeyID === "" || secretKey === ""}
                        startIcon={<KeyIcon />}
                      >
                        Set Keys
                      </Button>
                    )}
                  </div>
                  <div className={"pr-2"}>
                    <Button
                      variant={"contained"}
                      startIcon={<BarChartIcon />}
                      disabled={!keysSet}
                      onClick={loadValues}
                    >
                      Values
                    </Button>
                  </div>
                  <Button
                    variant={"contained"}
                    startIcon={<InsightsIcon />}
                    disabled={!keysSet}
                    onClick={loadScores}
                  >
                    Scores
                  </Button>
                </div>
              </div>
              {valuesSelected && (
                <>
                  {valuesLoaded ? (
                    <div className={"container-fluid"}>
                      {Object.keys(values).map((v) => (
                        <Stock title={v} values={values[v]} />
                      ))}
                    </div>
                  ) : (
                    <div className={"row pt-3"}>
                      <div
                        className={
                          "col d-flex align-items-center justify-content-center"
                        }
                      >
                        <CircularProgress size={"4em"} />
                      </div>
                    </div>
                  )}
                </>
              )}
              {scoresSelected && (
                <>
                  {scoresLoaded ? (
                    <div className={"row pt-4"}>
                      <div className={"col"}>Scores</div>
                    </div>
                  ) : (
                    <div className={"row pt-3"}>
                      <div
                        className={
                          "col d-flex align-items-center justify-content-center"
                        }
                      >
                        <CircularProgress size={"4em"} />
                      </div>
                    </div>
                  )}
                </>
              )}
            </>
          }
        />
      </div>
    </>
  );
};

export default Index;
