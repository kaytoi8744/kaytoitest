const CopyWebpackPlugin = require("copy-webpack-plugin");
const webpack = require("webpack");
const path = require("path");

module.exports = {
  mode: "development",
  entry: {
    "sign-in": "./src/sign-in.js",
  },
  output: {
    filename: "[name].bundle.js",
    path: path.resolve(__dirname, "dist"),
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
      },
    ],
  },
  devServer: {
    client: {
      overlay: true,
    },
    hot: true,
    watchFiles: ["src/*", "public/*"],
  },
  plugins: [
    new CopyWebpackPlugin({
      patterns: ["public"],
    }),
    new webpack.HotModuleReplacementPlugin(),
  ],
};