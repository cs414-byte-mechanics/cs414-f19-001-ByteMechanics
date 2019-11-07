while getopts u:p: option; do
  case "${option}" in
    u) USER=${OPTARG};;
    p) PORT=${OPTARG};;
  esac
done

if [ -z ${USER+x} ]; then
  echo -n "Enter your username and press [ENTER]: "
  read USER
fi
if [ -z ${PORT+x} ]; then
  echo -n "Enter your desired port and press [ENTER]: "
  read PORT
fi

ssh -N -L $PORT:faure.cs.colostate.edu:3306 $USER@shark.cs.colostate.edu